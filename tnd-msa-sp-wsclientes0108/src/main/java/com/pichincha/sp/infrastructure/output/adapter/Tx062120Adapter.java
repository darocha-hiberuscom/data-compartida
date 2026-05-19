package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.exception.BancsAPIClientException;
import com.pichincha.sp.application.port.output.CustomerFatcaDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx062120Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx062120Response;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Tx062120Adapter implements CustomerFatcaDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    public Tx062120Adapter(BancsClient bancsClient,
                            BancsDomainMapper bancsDomainMapper,
                            CustomLogLevelHandler customLogLevelHandler) {
        this.bancsClient = bancsClient;
        this.bancsDomainMapper = bancsDomainMapper;
        this.customLogLevelHandler = customLogLevelHandler;
    }

    @Override
    @BpLogger
    public Mono<CustomerFatcaDataUpdateResponseDto> updateFatcaData(CustomerFatcaDataUpdateRequestDto request) {
        Tx062120Request bodyDto = bancsDomainMapper.toTx062120Request(request);

        BancsRequest<Tx062120Request> bancsRequest = BancsRequest.<Tx062120Request>builder()
                .transactionId(BancsAdapterConstants.TX_062120)
                .body(bodyDto)
                .build();

        return bancsClient.call(bancsRequest, Tx062120Response.class)
                .map(bancsDomainMapper::toFatcaResponse)
                .onErrorMap(ex -> {
                    if (ex instanceof BancsAPIClientException) {
                        customLogLevelHandler.log(
                                CustomLogLevel.ERROR,
                                Thread.currentThread().getStackTrace(),
                                BancsAdapterConstants.LOG_BANCS_CLIENT_EXCEPTION,
                                BancsAdapterConstants.TX_062120,
                                ex.getMessage());
                        return new GlobalErrorException(
                                BancsAdapterConstants.ERROR_TX062120_HEADER,
                                BancsAdapterConstants.TX_062120,
                                BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                    }
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                            BancsAdapterConstants.TX_062120,
                            ex.getMessage() != null ? ex.getMessage() : "");
                    return new GlobalErrorException(
                            BancsAdapterConstants.ERROR_TX062120_HEADER + ": " + ex.getMessage(),
                            BancsAdapterConstants.TX_062120,
                            BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                })
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_062120,
                        signalType));
    }
}