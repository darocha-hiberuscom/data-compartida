package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.annotations.BancsService;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.port.output.CustomerPersonalDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067101Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067101Response;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class Tx067101Adapter extends AbstractBancsTransactionAdapter implements CustomerPersonalDataUpdateOutput {

    private final BancsDomainMapper mapper;

    public Tx067101Adapter(
            @BancsService("customer-profile") BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsDomainMapper mapper,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler);
        this.mapper = mapper;
    }

    @Override
    @BpLogger
    public Mono<CustomerPersonalDataUpdateResponseDto> updatePersonalData(CustomerPersonalDataUpdateRequestDto request) {
        Tx067101Request bodyDto = mapper.toTx067101Request(request);
        return executeTransaction(
                BancsAdapterConstants.TX_067101,
                bodyDto,
                Tx067101Response.class,
                mapper::toPersonalResponse);
    }
}
