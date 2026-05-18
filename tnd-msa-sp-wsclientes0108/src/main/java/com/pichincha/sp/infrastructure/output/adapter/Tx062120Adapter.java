package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.sp.application.port.output.CustomerFatcaDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class Tx062120Adapter implements CustomerFatcaDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
        private final BancsRequestFactory bancsRequestFactory;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CustomerFatcaDataUpdateResponseDto> updateFatcaData(CustomerFatcaDataUpdateRequestDto request) {
        Tx062120TechnicalRequest bodyDto = bancsDomainMapper.toTx062120Request(request);
        BancsRequest<Tx062120TechnicalRequest> bancsRequest = bancsRequestFactory
                .buildRequest(BancsAdapterConstants.TX_062120, bodyDto);

        return bancsClient.call(bancsRequest, Tx062120TechnicalResponse.class)
                .doOnNext(response -> customLogLevelHandler.log(CustomLogLevel.DEBUG, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_RESPONSE_RECEIVED, BancsAdapterConstants.TX_062120))
                .map(BancsResponse::body)
                .map(bancsDomainMapper::toFatcaResponse)
                .doFinally(signalType -> customLogLevelHandler.log(CustomLogLevel.INFO, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED, BancsAdapterConstants.TX_062120, signalType));
    }
}