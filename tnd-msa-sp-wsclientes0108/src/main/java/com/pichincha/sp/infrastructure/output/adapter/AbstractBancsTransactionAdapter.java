package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.adapter.BancsClient;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.response.BancsResponse;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevel;
import com.pichincha.common.trace.logger.logger.custom.level.CustomLogLevelHandler;
import com.pichincha.sp.application.exception.BancsIntegrationException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public abstract class AbstractBancsTransactionAdapter {

    private static final StackTraceElement[] SAFE_STACK_TRACE = new StackTraceElement[0];

    private final BancsClient bancsClient;
    private final CustomLogLevelHandler customLogLevelHandler;
    private final BancsErrorHandlerAdapter bancsAdapterErrorHandler;

    protected AbstractBancsTransactionAdapter(
            BancsClient bancsClient,
            CustomLogLevelHandler customLogLevelHandler,
            BancsErrorHandlerAdapter bancsAdapterErrorHandler) {
        this.bancsClient = bancsClient;
        this.customLogLevelHandler = customLogLevelHandler;
        this.bancsAdapterErrorHandler = bancsAdapterErrorHandler;
    }

    protected <RequestType, ResponseType, DomainType> Mono<DomainType> executeTransaction(
            String transactionId,
            RequestType requestBody,
            Class<ResponseType> responseClass,
            Function<ResponseType, DomainType> mapper) {
        return executeTransactionInternal(transactionId, requestBody, responseClass, null, mapper);
    }

    protected <RequestType, ResponseType, DomainType> Mono<DomainType> executeTransaction(
            String transactionId,
            RequestType requestBody,
            Class<ResponseType> responseClass,
            String operationPath,
            Function<ResponseType, DomainType> mapper) {
        return executeTransactionInternal(transactionId, requestBody, responseClass, operationPath, mapper);
    }

    private <RequestType, ResponseType, DomainType> Mono<DomainType> executeTransactionInternal(
            String transactionId,
            RequestType requestBody,
            Class<ResponseType> responseClass,
            String operationPath,
            Function<ResponseType, DomainType> mapper) {

        BancsRequest<RequestType> bancsRequest = BancsRequest.<RequestType>builder()
                .transactionId(transactionId)
                .body(requestBody)
                .build();

        Mono<BancsResponse<ResponseType>> callMono = operationPath == null || operationPath.isBlank()
                ? bancsClient.call(bancsRequest, responseClass)
                : bancsClient.call(bancsRequest, responseClass, operationPath);

        return callMono
                .doOnNext(response -> customLogLevelHandler.log(
                        CustomLogLevel.DEBUG,
                        SAFE_STACK_TRACE,
                        "BANCS response received",
                        response))
                .flatMap(response -> extractBody(transactionId, response))
                .map(mapper)
                .onErrorResume(BancsIntegrationException.class,
                        ex -> bancsAdapterErrorHandler.handleBancsIntegrationError(ex, transactionId))
                .onErrorResume(WebClientResponseException.class,
                        ex -> bancsAdapterErrorHandler.handleHttpError(ex, transactionId))
                .onErrorResume(WebClientRequestException.class,
                        ex -> bancsAdapterErrorHandler.handleConnectionError(ex, transactionId))
                .onErrorResume(TimeoutException.class,
                        ex -> bancsAdapterErrorHandler.handleTimeoutError(ex, transactionId))
                .doOnError(error -> customLogLevelHandler.log(
                        CustomLogLevel.ERROR,
                        SAFE_STACK_TRACE,
                        "BANCS unexpected error",
                        error.getMessage()))
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        SAFE_STACK_TRACE,
                        "BANCS adapter flow finished",
                        signalType));
    }

    private <ResponseType> Mono<ResponseType> extractBody(String transactionId, BancsResponse<ResponseType> response) {
        if (response == null || (response.body() == null && response.error() == null)) {
            return bancsAdapterErrorHandler.handleEmptyBodyError(transactionId);
        }
        if (Boolean.FALSE.equals(response.success())) {
            String errorCode = response.error() != null ? response.error().code() : null;
            String errorMessage = response.error() != null ? response.error().message() : null;
            return bancsAdapterErrorHandler.handleUnsuccessfulResponseError(
                    transactionId,
                    errorCode,
                    errorMessage);
        }
        ResponseType responseBody = response.body();
        return responseBody != null
                ? Mono.just(responseBody)
                : bancsAdapterErrorHandler.handleEmptyBodyError(transactionId);
    }
}
