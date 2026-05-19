package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.exception.BancsAPIClientException;
import com.pichincha.sp.application.port.output.CustomerAdditionalDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067116Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067116Response;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Tx067116Adapter implements CustomerAdditionalDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    public Tx067116Adapter(BancsClient bancsClient,
                            BancsDomainMapper bancsDomainMapper,
                            CustomLogLevelHandler customLogLevelHandler) {
        this.bancsClient = bancsClient;
        this.bancsDomainMapper = bancsDomainMapper;
        this.customLogLevelHandler = customLogLevelHandler;
    }

    @Override
    @BpLogger
    public Mono<CustomerAdditionalDataUpdateResponseDto> updateAdditionalData(CustomerAdditionalDataUpdateRequestDto request) {
        Tx067116Request bodyDto = bancsDomainMapper.toTx067116Request(request);

        BancsRequest<Tx067116Request> bancsRequest = BancsRequest.<Tx067116Request>builder()
                .transactionId(BancsAdapterConstants.TX_067116)
                .body(bodyDto)
                .build();

        return bancsClient.call(bancsRequest, Tx067116Response.class)
                .map(bancsDomainMapper::toAdditionalResponse)
                .onErrorMap(ex -> {
                    if (ex instanceof BancsAPIClientException) {
                        customLogLevelHandler.log(
                                CustomLogLevel.ERROR,
                                Thread.currentThread().getStackTrace(),
                                BancsAdapterConstants.LOG_BANCS_CLIENT_EXCEPTION,
                                BancsAdapterConstants.TX_067116,
                                ex.getMessage());
                        return new GlobalErrorException(
                                BancsAdapterConstants.ERROR_TX067116_HEADER,
                                BancsAdapterConstants.TX_067116,
                                BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                    }
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                            BancsAdapterConstants.TX_067116,
                            ex.getMessage() != null ? ex.getMessage() : "");
                    return new GlobalErrorException(
                            BancsAdapterConstants.ERROR_TX067116_HEADER + ": " + ex.getMessage(),
                            BancsAdapterConstants.TX_067116,
                            BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                })
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_067116,
                        signalType));
    }
}
