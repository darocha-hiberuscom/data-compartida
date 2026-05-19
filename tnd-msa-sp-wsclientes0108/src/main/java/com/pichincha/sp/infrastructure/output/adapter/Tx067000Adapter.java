package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.exception.BancsAPIClientException;
import com.pichincha.sp.application.port.output.CustomerBasicDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067000Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067000Response;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Tx067000Adapter implements CustomerBasicDataUpdateOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    public Tx067000Adapter(BancsClient bancsClient,
                            BancsDomainMapper bancsDomainMapper,
                            CustomLogLevelHandler customLogLevelHandler) {
        this.bancsClient = bancsClient;
        this.bancsDomainMapper = bancsDomainMapper;
        this.customLogLevelHandler = customLogLevelHandler;
    }

    @Override
    @BpLogger
    public Mono<CustomerBasicDataUpdateResponseDto> updateBasicData(CustomerBasicDataUpdateRequestDto request) {
        Tx067000Request bodyDto = bancsDomainMapper.toTx067000Request(request);

        BancsRequest<Tx067000Request> bancsRequest = BancsRequest.<Tx067000Request>builder()
                .transactionId(BancsAdapterConstants.TX_067000)
                .body(bodyDto)
                .build();

        return bancsClient.call(bancsRequest, Tx067000Response.class)
                .map(bancsDomainMapper::toBasicResponse)
                .onErrorMap(ex -> {
                    if (ex instanceof BancsAPIClientException) {
                        customLogLevelHandler.log(
                                CustomLogLevel.ERROR,
                                Thread.currentThread().getStackTrace(),
                                BancsAdapterConstants.LOG_BANCS_CLIENT_EXCEPTION,
                                BancsAdapterConstants.TX_067000,
                                ex.getMessage());
                        return new GlobalErrorException(
                                BancsAdapterConstants.ERROR_TX067000_HEADER,
                                BancsAdapterConstants.TX_067000,
                                BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                    }
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                            BancsAdapterConstants.TX_067000,
                            ex.getMessage() != null ? ex.getMessage() : "");
                    return new GlobalErrorException(
                            BancsAdapterConstants.ERROR_TX067000_HEADER + ": " + ex.getMessage(),
                            BancsAdapterConstants.TX_067000,
                            BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                })
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_067000,
                        signalType));
    }
}
