package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.sp.application.port.output.CustomerBasicDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class Tx067000Adapter implements CustomerBasicDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
        private final BancsRequestFactory bancsRequestFactory;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CustomerBasicDataUpdateResponseDto> updateBasicData(CustomerBasicDataUpdateRequestDto request) {
        Tx067000TechnicalRequest bodyDto = bancsDomainMapper.toTx067000Request(request);
        BancsRequest<Tx067000TechnicalRequest> bancsRequest = bancsRequestFactory
                .buildRequest(BancsAdapterConstants.TX_067000, bodyDto);

        return bancsClient.call(bancsRequest, Tx067000TechnicalResponse.class)
                .doOnNext(response -> customLogLevelHandler.log(CustomLogLevel.DEBUG, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_RESPONSE_RECEIVED, BancsAdapterConstants.TX_067000))
                .map(BancsResponse::body)
                .map(bancsDomainMapper::toBasicResponse)
                .doFinally(signalType -> customLogLevelHandler.log(CustomLogLevel.INFO, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED, BancsAdapterConstants.TX_067000, signalType));
    }
}
