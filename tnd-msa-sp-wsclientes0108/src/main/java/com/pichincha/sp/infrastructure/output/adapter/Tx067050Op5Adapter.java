package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.exception.BancsAPIClientException;
import com.pichincha.sp.application.port.output.CustomerPersonalDetailOutput;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067050Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067050Response;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Tx067050Op5Adapter implements CustomerPersonalDetailOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    public Tx067050Op5Adapter(BancsClient bancsClient,
                               BancsDomainMapper bancsDomainMapper,
                               CustomLogLevelHandler customLogLevelHandler) {
        this.bancsClient = bancsClient;
        this.bancsDomainMapper = bancsDomainMapper;
        this.customLogLevelHandler = customLogLevelHandler;
    }

    @Override
    @BpLogger
    public Mono<CustomerLookupResponseDto> getCustomerPersonalDetail(CustomerLookupRequestDto request) {
        Tx067050Request bodyDto = bancsDomainMapper.toTx067050Request(request);

        BancsRequest<Tx067050Request> bancsRequest = BancsRequest.<Tx067050Request>builder()
                .transactionId(BancsAdapterConstants.TX_067050)
                .body(bodyDto)
                .build();

        return bancsClient.call(bancsRequest, Tx067050Response.class, BancsAdapterConstants.TX_067050_OP_5)
                .map(bancsDomainMapper::toLookupResponse)
                .onErrorMap(ex -> {
                    if (ex instanceof BancsAPIClientException) {
                        customLogLevelHandler.log(
                                CustomLogLevel.ERROR,
                                Thread.currentThread().getStackTrace(),
                                BancsAdapterConstants.LOG_BANCS_CLIENT_EXCEPTION,
                                BancsAdapterConstants.TX_067050_OP_5,
                                ex.getMessage());
                        return new GlobalErrorException(
                                BancsAdapterConstants.ERROR_TX067050_OP5_HEADER,
                                BancsAdapterConstants.TX_067050_OP_5,
                                BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                    }
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                            BancsAdapterConstants.TX_067050_OP_5,
                            ex.getMessage() != null ? ex.getMessage() : "");
                    return new GlobalErrorException(
                            BancsAdapterConstants.ERROR_TX067050_OP5_HEADER + ": " + ex.getMessage(),
                            BancsAdapterConstants.TX_067050_OP_5,
                            BancsAdapterConstants.DEFAULT_ERROR_STATUS);
                })
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_067050_OP_5,
                        signalType));
    }
}
