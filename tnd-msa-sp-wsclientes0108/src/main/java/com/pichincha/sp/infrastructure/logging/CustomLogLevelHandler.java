package com.pichincha.sp.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomLogLevelHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogLevelHandler.class);

    public void log(CustomLogLevel level, StackTraceElement[] stackTrace, String message, Object... params) {
        if (level == null) {
            LOGGER.info(message, params);
            return;
        }
        switch (level) {
            case DEBUG -> LOGGER.debug(message, params);
            case INFO -> LOGGER.info(message, params);
            case WARN -> LOGGER.warn(message, params);
            case ERROR -> LOGGER.error(message, params);
            default -> LOGGER.info(message, params);
        }
    }
}
