package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.annotations.BancsService;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.port.output.CustomerIdentificationUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067153Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067153Response;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class Tx067153Adapter extends AbstractBancsTransactionAdapter implements CustomerIdentificationUpdateOutput {

    private final BancsDomainMapper mapper;

    public Tx067153Adapter(
            @BancsService("customer-profile") BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsDomainMapper mapper,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler);
        this.mapper = mapper;
    }

    @Override
    @BpLogger
    public Mono<CustomerIdentificationUpdateResponseDto> queryIdentification(CustomerIdentificationUpdateRequestDto request) {
        return executeTransactionInternal(request);
    }

    @Override
    @BpLogger
    public Mono<CustomerIdentificationUpdateResponseDto> updateIdentification(CustomerIdentificationUpdateRequestDto request) {
        return executeTransactionInternal(request);
    }

    private Mono<CustomerIdentificationUpdateResponseDto> executeTransactionInternal(CustomerIdentificationUpdateRequestDto request) {
        Tx067153Request bodyDto = mapper.toTx067153Request(request);
        return executeTransaction(
                BancsAdapterConstants.TX_067153,
                bodyDto,
                Tx067153Response.class,
                mapper::toIdentificationResponse);
    }
}
