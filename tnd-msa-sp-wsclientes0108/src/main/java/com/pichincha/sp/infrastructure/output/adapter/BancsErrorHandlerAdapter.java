package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.sp.application.exception.BancsIntegrationException;
import com.pichincha.sp.application.exception.BusinessException;
import com.pichincha.sp.domain.dto.BusinessErrorDto;
import java.util.Locale;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BancsErrorHandlerAdapter {

    private static final String EMPTY_STRING = "";
    private static final String BLANK_SEPARATOR = " ";
    private static final String TX_PREFIX = "TX";
    private static final Pattern NON_DIGIT_PATTERN = Pattern.compile("\\D");

    @Value("${spring.application.name:tnd-msa-sp-wsclientes0114}")
    private String applicationName;

    @Value("${backend.bancs.component-name:tnd-msa-sp-wsclientes0114}")
    private String componentName;

    @Value("${backend.bancs.code:00638}")
    private String backendCode;

    @Value("${bancs.error-handler.http-error-code:9929}")
    private String httpErrorCode;

    @Value("${bancs.error-handler.http-error-message:Error al invocar transaccion Bancs}")
    private String httpErrorMessage;

    @Value("${bancs.error-handler.missing-teller-code:9927}")
    private String missingTellerCode;

    @Value("${bancs.error-handler.missing-teller-message:Datos de la cabecera de la transaccion no se han asignado}")
    private String missingTellerMessage;

    @Value("${bancs.error-handler.integration-error-code:9928}")
    private String integrationErrorCode;

    @Value("${bancs.error-handler.integration-error-message:ESB-%2%}")
    private String integrationErrorMessage;

    @Value("${bancs.error-handler.timeout-code:9920}")
    private String timeoutErrorCode;

    @Value("${bancs.error-handler.timeout-message:Tiempo de espera terminado sin respuesta}")
    private String timeoutErrorMessage;

    @Value("${bancs.error-handler.empty-body-code:9928}")
    private String emptyBodyErrorCode;

    @Value("${bancs.error-handler.empty-body-message:Error al invocar transaccion Bancs}")
    private String emptyBodyErrorMessage;

    @Value("${bancs.error-handler.fatal-type:FATAL}")
    private String fatalType;

    @Value("${bancs.error-handler.resource-template:%s/%s}")
    private String resourceTemplate;

    @Value("${bancs.error-handler.operation-default:operacion-desconocida}")
    private String operationDefault;

    @Value("${validation.bancs.teller-header-name:x-teller}")
    private String tellerHeaderName;

    @Value("${validation.bancs.missing-teller-token:teller}")
    private String missingTellerToken;

    public <T> Mono<T> handleBancsIntegrationError(BancsIntegrationException exception, String operationName) {
        return Mono.error(new BusinessException(
                buildBusinessError(
                        integrationErrorCode,
                        buildMessageWithException(integrationErrorMessage, exception),
                        operationName,
                        false),
                exception));
    }

    public <T> Mono<T> handleHttpError(WebClientResponseException exception, String operationName) {
        String code = resolveHttpErrorCode(exception);
        String message = resolveHttpErrorMessage(exception);
        return Mono.error(new BusinessException(
                buildBusinessError(code, message, operationName, false),
                exception));
    }

    public <T> Mono<T> handleConnectionError(WebClientRequestException exception, String operationName) {
        return Mono.error(new BusinessException(
                buildBusinessError(
                        integrationErrorCode,
                        buildMessageWithException(integrationErrorMessage, exception),
                        operationName,
                        false),
                exception));
    }

    public <T> Mono<T> handleTimeoutError(TimeoutException exception, String operationName) {
        return Mono.error(new BusinessException(
                buildBusinessError(timeoutErrorCode, timeoutErrorMessage, operationName, false),
                exception));
    }

    public <T> Mono<T> handleEmptyBodyError(String operationName) {
        return Mono.error(new BusinessException(
                buildBusinessError(emptyBodyErrorCode, emptyBodyErrorMessage, operationName, false)));
    }

    public <T> Mono<T> handleUnsuccessfulResponseError(String operationName, String errorCode, String errorMessage) {
        String resolvedCode = (errorCode == null || errorCode.isBlank())
                ? integrationErrorCode
                : errorCode;
        String resolvedMessage = (errorMessage == null || errorMessage.isBlank())
                ? httpErrorMessage
                : errorMessage;
        return Mono.error(new BusinessException(
                buildBusinessError(resolvedCode, resolvedMessage, operationName, true)));
    }

    private String resolveHttpErrorCode(WebClientResponseException exception) {
        if (exception != null && exception.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            return httpErrorCode;
        }
        if (isMissingTellerHeaderError(exception)) {
            return missingTellerCode;
        }
        return httpErrorCode;
    }

    private String resolveHttpErrorMessage(WebClientResponseException exception) {
        if (exception != null && exception.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            return httpErrorMessage;
        }
        if (isMissingTellerHeaderError(exception)) {
            return missingTellerMessage;
        }
        return buildMessageWithException(httpErrorMessage, exception);
    }

    private String buildErrorResource(String operationName) {
        String resolvedOperation = (operationName == null || operationName.isBlank())
                ? operationDefault
                : operationName;
        return String.format(resourceTemplate, resolveApplicationName(), resolvedOperation);
    }

    private String resolveApplicationName() {
        if (applicationName != null && !applicationName.isBlank()) {
            return applicationName;
        }
        return componentName;
    }

    private static String buildMessageWithException(String baseMessage, Throwable exception) {
        String resolvedBase = baseMessage == null ? EMPTY_STRING : baseMessage.trim();
        String exceptionMessage = exception != null ? exception.getMessage() : null;

        if ((exceptionMessage == null || exceptionMessage.isBlank()) && exception != null && exception.getCause() != null) {
            exceptionMessage = exception.getCause().getMessage();
        }

        if (exceptionMessage == null || exceptionMessage.isBlank()) {
            return resolvedBase;
        }

        if (resolvedBase.isBlank()) {
            return exceptionMessage;
        }

        return resolvedBase + BLANK_SEPARATOR + exceptionMessage;
    }

    private BusinessErrorDto buildBusinessError(String code, String message, String operationName, boolean useTransactionComponent) {
        return BusinessErrorDto.builder()
                .code(code)
                .message(message)
                .businessMessage(EMPTY_STRING)
                .type(fatalType)
                .resource(buildErrorResource(operationName))
                .component(resolveBancsComponent(operationName, useTransactionComponent))
                .backend(backendCode)
                .build();
    }

    private String resolveBancsComponent(String operationName, boolean useTransactionComponent) {
        String resolvedComponent = componentName;
        if (useTransactionComponent && operationName != null && !operationName.isBlank()) {
            String numericOperation = NON_DIGIT_PATTERN.matcher(operationName).replaceAll(EMPTY_STRING);
            if (!numericOperation.isBlank()) {
                resolvedComponent = TX_PREFIX + numericOperation;
            }
        }
        return resolvedComponent;
    }

    private boolean isMissingTellerHeaderError(WebClientResponseException exception) {
        if (exception == null) {
            return false;
        }
        String normalized = String.valueOf(exception.getMessage()).toLowerCase(Locale.ROOT);
        if (normalized.isBlank()) {
            return false;
        }
        return containsToken(normalized, tellerHeaderName)
                || containsToken(normalized, missingTellerToken);
    }

    private static boolean containsToken(String source, String token) {
        return token != null
                && !token.isBlank()
                && source.contains(token.toLowerCase(Locale.ROOT));
    }
}

