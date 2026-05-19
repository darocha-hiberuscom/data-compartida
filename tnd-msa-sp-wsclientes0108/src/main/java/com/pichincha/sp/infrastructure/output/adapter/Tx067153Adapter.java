package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.exception.BancsAPIClientException;
import com.pichincha.sp.application.port.output.CustomerIdentificationUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067153Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067153Response;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Tx067153Adapter implements CustomerIdentificationUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    public Tx067153Adapter(BancsClient bancsClient,
                            BancsDomainMapper bancsDomainMapper,
                            CustomLogLevelHandler customLogLevelHandler) {
        this.bancsClient = bancsClient;
        this.bancsDomainMapper = bancsDomainMapper;
        this.customLogLevelHandler = customLogLevelHandler;
    }

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
        Tx067153Request bodyDto = bancsDomainMapper.toTx067153Request(request);

        BancsRequest<Tx067153Request> bancsRequest = BancsRequest.<Tx067153Request>builder()
                .transactionId(BancsAdapterConstants.TX_067153)
                .body(bodyDto)
                .build();

        return bancsClient.call(bancsRequest, Tx067153Response.class)
                .map(bancsDomainMapper::toIdentificationResponse)
                .onErrorMap(ex -> {
                    if (ex instanceof BancsAPIClientException) {
                        customLogLevelHandler.log(
                                CustomLogLevel.ERROR,
                                Thread.currentThread().getStackTrace(),
                                BancsAdapterConstants.LOG_BANCS_CLIENT_EXCEPTION,
                                BancsAdapterConstants.TX_067153,
                                ex.getMessage());
                        return new GlobalErrorException(
                                BancsAdapterConstants.ERROR_TX067153_HEADER,
                                BancsAdapterConstants.TX_067153,
                                BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                    }
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                            BancsAdapterConstants.TX_067153,
                            ex.getMessage() != null ? ex.getMessage() : "");
                    return new GlobalErrorException(
                            BancsAdapterConstants.ERROR_TX067153_HEADER + ": " + ex.getMessage(),
                            BancsAdapterConstants.TX_067153,
                            BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                })
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_067153,
                        signalType));
    }
}
