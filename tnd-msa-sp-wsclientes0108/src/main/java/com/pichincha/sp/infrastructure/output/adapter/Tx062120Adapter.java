package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.annotations.BancsService;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.port.output.CustomerFatcaDataUpdateOutput;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx062120Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx062120Response;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class Tx062120Adapter extends AbstractBancsTransactionAdapter implements CustomerFatcaDataUpdateOutput {

    private final BancsDomainMapper mapper;

    public Tx062120Adapter(
            @BancsService("customer-profile") BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsDomainMapper mapper,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler);
        this.mapper = mapper;
    }

    @Override
    @BpLogger
    public Mono<CustomerFatcaDataUpdateResponseDto> updateFatcaData(CustomerFatcaDataUpdateRequestDto request) {
        Tx062120Request bodyDto = mapper.toTx062120Request(request);
        return executeTransaction(
                BancsAdapterConstants.TX_062120,
                bodyDto,
                Tx062120Response.class,
                mapper::toFatcaResponse);
    }
}