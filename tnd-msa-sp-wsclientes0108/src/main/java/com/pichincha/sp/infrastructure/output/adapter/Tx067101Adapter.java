package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.sp.application.port.output.CustomerPersonalDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class Tx067101Adapter implements CustomerPersonalDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
        private final BancsRequestFactory bancsRequestFactory;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CustomerPersonalDataUpdateResponseDto> updatePersonalData(CustomerPersonalDataUpdateRequestDto request) {
        Tx067101TechnicalRequest bodyDto = bancsDomainMapper.toTx067101Request(request);
        BancsRequest<Tx067101TechnicalRequest> bancsRequest = bancsRequestFactory
                .buildRequest(BancsAdapterConstants.TX_067101, bodyDto);

        return bancsClient.call(bancsRequest, Tx067101TechnicalResponse.class)
                .doOnNext(response -> customLogLevelHandler.log(CustomLogLevel.DEBUG, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_RESPONSE_RECEIVED, BancsAdapterConstants.TX_067101))
                .map(BancsResponse::body)
                .map(bancsDomainMapper::toPersonalResponse)
                .doFinally(signalType -> customLogLevelHandler.log(CustomLogLevel.INFO, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED, BancsAdapterConstants.TX_067101, signalType));
    }
}
