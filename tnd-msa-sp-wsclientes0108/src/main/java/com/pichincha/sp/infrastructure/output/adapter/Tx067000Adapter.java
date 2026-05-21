package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.annotations.BancsService;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.port.output.CustomerBasicDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067000Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067000Response;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class Tx067000Adapter extends AbstractBancsTransactionAdapter implements CustomerBasicDataUpdateOutput {

    private final BancsDomainMapper mapper;

    public Tx067000Adapter(
            @BancsService("customer-profile") BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsDomainMapper mapper,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler);
        this.mapper = mapper;
    }

    @Override
    @BpLogger
    public Mono<CustomerBasicDataUpdateResponseDto> updateBasicData(CustomerBasicDataUpdateRequestDto request) {
        Tx067000Request bodyDto = mapper.toTx067000Request(request);
        return executeTransaction(
                BancsAdapterConstants.TX_067000,
                bodyDto,
                Tx067000Response.class,
                mapper::toBasicResponse);
    }
}
