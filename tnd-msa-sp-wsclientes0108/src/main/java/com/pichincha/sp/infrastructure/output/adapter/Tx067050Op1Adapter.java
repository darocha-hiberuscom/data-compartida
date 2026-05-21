package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
 import com.pichincha.bnc.apiclient.annotations.BancsService;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.port.output.CustomerDetailCreateOutput;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupResponseDto;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import com.pichincha.sp.infrastructure.output.adapter.mapper.BancsDomainMapper;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067050Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067050Response;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class Tx067050Op1Adapter extends AbstractBancsTransactionAdapter implements CustomerDetailCreateOutput {

    private final BancsDomainMapper mapper;

    public Tx067050Op1Adapter(
            @BancsService("customer-profile") BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsDomainMapper mapper,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler);
        this.mapper = mapper;
    }

    @Override
    @BpLogger
    public Mono<CustomerLookupResponseDto> createCustomerDetail(CustomerLookupRequestDto request) {
        Tx067050Request bodyDto = mapper.toTx067050Request(request);
        return executeTransaction(
                BancsAdapterConstants.TX_067050,
                bodyDto,
                Tx067050Response.class,
                BancsAdapterConstants.TX_067050_OP_1,
                mapper::toLookupResponse);
    }
}
