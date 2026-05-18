package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.sp.application.port.output.CustomerAdditionalDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class Tx067116Adapter implements CustomerAdditionalDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
        private final BancsRequestFactory bancsRequestFactory;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CustomerAdditionalDataUpdateResponseDto> updateAdditionalData(CustomerAdditionalDataUpdateRequestDto request) {
        Tx067116TechnicalRequest bodyDto = bancsDomainMapper.toTx067116Request(request);
        BancsRequest<Tx067116TechnicalRequest> bancsRequest = bancsRequestFactory
                .buildRequest(BancsAdapterConstants.TX_067116, bodyDto);

        return bancsClient.call(bancsRequest, Tx067116TechnicalResponse.class)
                .doOnNext(response -> customLogLevelHandler.log(CustomLogLevel.DEBUG, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_RESPONSE_RECEIVED, BancsAdapterConstants.TX_067116))
                .map(BancsResponse::body)
                .map(bancsDomainMapper::toAdditionalResponse)
                .doFinally(signalType -> customLogLevelHandler.log(CustomLogLevel.INFO, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED, BancsAdapterConstants.TX_067116, signalType));
    }
}
