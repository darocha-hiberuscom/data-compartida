package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.exception.BancsAPIClientException;
import com.pichincha.sp.application.port.output.CustomerPersonalDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067101Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067101Response;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Tx067101Adapter implements CustomerPersonalDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    public Tx067101Adapter(BancsClient bancsClient,
                            BancsDomainMapper bancsDomainMapper,
                            CustomLogLevelHandler customLogLevelHandler) {
        this.bancsClient = bancsClient;
        this.bancsDomainMapper = bancsDomainMapper;
        this.customLogLevelHandler = customLogLevelHandler;
    }

    @Override
    @BpLogger
    public Mono<CustomerPersonalDataUpdateResponseDto> updatePersonalData(CustomerPersonalDataUpdateRequestDto request) {
        Tx067101Request bodyDto = bancsDomainMapper.toTx067101Request(request);

        BancsRequest<Tx067101Request> bancsRequest = BancsRequest.<Tx067101Request>builder()
                .transactionId(BancsAdapterConstants.TX_067101)
                .body(bodyDto)
                .build();

        return bancsClient.call(bancsRequest, Tx067101Response.class)
                .map(bancsDomainMapper::toPersonalResponse)
                .onErrorMap(ex -> {
                    if (ex instanceof BancsAPIClientException) {
                        customLogLevelHandler.log(
                                CustomLogLevel.ERROR,
                                Thread.currentThread().getStackTrace(),
                                BancsAdapterConstants.LOG_BANCS_CLIENT_EXCEPTION,
                                BancsAdapterConstants.TX_067101,
                                ex.getMessage());
                        return new GlobalErrorException(
                                BancsAdapterConstants.ERROR_TX067101_HEADER,
                                BancsAdapterConstants.TX_067101,
                                BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                    }
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                            BancsAdapterConstants.TX_067101,
                            ex.getMessage() != null ? ex.getMessage() : "");
                    return new GlobalErrorException(
                            BancsAdapterConstants.ERROR_TX067101_HEADER + ": " + ex.getMessage(),
                            BancsAdapterConstants.TX_067101,
                            BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                })
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_067101,
                        signalType));
    }
}
