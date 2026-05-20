package com.pichincha.sp.infrastructure.logging.adapter;

import com.pichincha.sp.application.port.output.ApplicationLogger;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementación de ApplicationLogger en infrastructure.
 * Adapta CustomLogLevelHandler para que cumpla con port de application.
 */
@Component
@RequiredArgsConstructor
public class ApplicationLoggerImpl implements ApplicationLogger {

    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    public void logFlowFinished(String signal) {
        customLogLevelHandler.log(
                CustomLogLevel.INFO,
                Thread.currentThread().getStackTrace(),
                "Create customer flow finished with signal: {}",
                signal);
    }

    @Override
    public void logFlowError(String message) {
        customLogLevelHandler.log(
                CustomLogLevel.ERROR,
                Thread.currentThread().getStackTrace(),
                "Create customer flow error: {}",
                message);
    }

    @Override
    public void logFlowStarted(String message) {
        customLogLevelHandler.log(
                CustomLogLevel.INFO,
                Thread.currentThread().getStackTrace(),
                "Create customer flow started: {}",
                message);
    }
}

