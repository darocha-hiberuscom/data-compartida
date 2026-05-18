package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.sp.application.port.output.CustomerIdentificationUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class Tx067153Adapter implements CustomerIdentificationUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final BancsRequestFactory bancsRequestFactory;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CustomerIdentificationUpdateResponseDto> queryIdentification(CustomerIdentificationUpdateRequestDto request) {
        return executeTransaction(request);
    }

    @Override
    @BpLogger
    public Mono<CustomerIdentificationUpdateResponseDto> updateIdentification(CustomerIdentificationUpdateRequestDto request) {
        return executeTransaction(request);
    }

    private Mono<CustomerIdentificationUpdateResponseDto> executeTransaction(CustomerIdentificationUpdateRequestDto request) {
        Tx067153TechnicalRequest bodyDto = bancsDomainMapper.toTx067153Request(request);
        BancsRequest<Tx067153TechnicalRequest> bancsRequest = bancsRequestFactory
            .buildRequest(BancsAdapterConstants.TX_067153, bodyDto);

        return bancsClient.call(bancsRequest, Tx067153TechnicalResponse.class)
                .doOnNext(response -> customLogLevelHandler.log(CustomLogLevel.DEBUG, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_RESPONSE_RECEIVED, BancsAdapterConstants.TX_067153))
            .map(BancsResponse::body)
                .map(bancsDomainMapper::toIdentificationResponse)
                .doFinally(signalType -> customLogLevelHandler.log(CustomLogLevel.INFO, Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED, BancsAdapterConstants.TX_067153, signalType));
    }
}
