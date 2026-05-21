package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.annotations.BancsService;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.port.output.CustomerAdditionalDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067116Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067116Response;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class Tx067116Adapter extends AbstractBancsTransactionAdapter implements CustomerAdditionalDataUpdateOutput {

    private final BancsDomainMapper mapper;

    public Tx067116Adapter(
            @BancsService("customer-profile") BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsDomainMapper mapper,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler);
        this.mapper = mapper;
    }

    @Override
    @BpLogger
    public Mono<CustomerAdditionalDataUpdateResponseDto> updateAdditionalData(CustomerAdditionalDataUpdateRequestDto request) {
        Tx067116Request bodyDto = mapper.toTx067116Request(request);
        return executeTransaction(
                BancsAdapterConstants.TX_067116,
                bodyDto,
                Tx067116Response.class,
                mapper::toAdditionalResponse);
    }
}
