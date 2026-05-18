package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.sp.application.port.output.CustomerLookupOutput;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupResponseDto;
import com.pichincha.sp.domain.exception.BusinessException;
import com.pichincha.sp.infrastructure.constants.BancsAdapterConstants;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

@Repository
@RequiredArgsConstructor
public class Tx067050Adapter implements CustomerLookupOutput {

    private final BancsClient bancsClient;
    private final BancsDomainMapper bancsDomainMapper;
        private final BancsRequestFactory bancsRequestFactory;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CustomerLookupResponseDto> findCustomer(CustomerLookupRequestDto request) {
        Tx067050TechnicalRequest bodyDto = bancsDomainMapper.toTx067050Request(request);
        BancsRequest<Tx067050TechnicalRequest> bancsRequest = bancsRequestFactory
                .buildRequest(BancsAdapterConstants.TX_067050, bodyDto);

        return bancsClient.call(bancsRequest, Tx067050TechnicalResponse.class)
                .doOnNext(response -> customLogLevelHandler.log(
                        CustomLogLevel.DEBUG,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_RESPONSE_RECEIVED,
                        BancsAdapterConstants.TX_067050))
                .map(BancsResponse::body)
                .flatMap(this::extractResponse)
                .doOnNext(response -> customLogLevelHandler.log(
                        CustomLogLevel.DEBUG,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_VALIDATION_SUCCESS,
                        BancsAdapterConstants.TX_067050))
                .map(bancsDomainMapper::toLookupResponse)
                .doOnNext(response -> customLogLevelHandler.log(
                        CustomLogLevel.DEBUG,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_MAPPING_SUCCESS,
                        BancsAdapterConstants.TX_067050))
                .onErrorResume(WebClientResponseException.class, ex -> handleHttpError(BancsAdapterConstants.TX_067050, ex))
                .onErrorResume(WebClientRequestException.class, ex -> handleConnectionError(BancsAdapterConstants.TX_067050, ex))
                .onErrorResume(TimeoutException.class, ex -> handleTimeoutError(BancsAdapterConstants.TX_067050, ex))
                .doOnError(error -> customLogLevelHandler.log(
                        CustomLogLevel.ERROR,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_UNEXPECTED_ERROR,
                        BancsAdapterConstants.TX_067050,
                        error.getMessage() == null ? "" : error.getMessage()))
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        BancsAdapterConstants.LOG_FLOW_FINISHED,
                        BancsAdapterConstants.TX_067050,
                        signalType));
    }

    private Mono<Tx067050TechnicalResponse> extractResponse(Tx067050TechnicalResponse response) {
        if (response == null) {
            return Mono.error(new BusinessException("BANCS response body is null for transaction 067050"));
        }
        return Mono.just(response);
    }

    private <T> Mono<T> handleHttpError(String transactionId, WebClientResponseException exception) {
        customLogLevelHandler.log(CustomLogLevel.ERROR, Thread.currentThread().getStackTrace(),
                BancsAdapterConstants.LOG_HTTP_ERROR, transactionId, exception.getMessage() == null ? "" : exception.getMessage());
        return Mono.error(new BusinessException("BANCS http integration error", exception));
    }

    private <T> Mono<T> handleConnectionError(String transactionId, WebClientRequestException exception) {
        customLogLevelHandler.log(CustomLogLevel.ERROR, Thread.currentThread().getStackTrace(),
                BancsAdapterConstants.LOG_CONNECTION_ERROR, transactionId, exception.getMessage() == null ? "" : exception.getMessage());
        return Mono.error(new BusinessException("BANCS connection integration error", exception));
    }

    private <T> Mono<T> handleTimeoutError(String transactionId, TimeoutException exception) {
        customLogLevelHandler.log(CustomLogLevel.ERROR, Thread.currentThread().getStackTrace(),
                BancsAdapterConstants.LOG_TIMEOUT_ERROR, transactionId, exception.getMessage() == null ? "" : exception.getMessage());
        return Mono.error(new BusinessException("BANCS timeout integration error", exception));
    }
}
