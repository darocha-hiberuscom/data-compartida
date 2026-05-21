package com.pichincha.sp.application.exception;

public class BancsIntegrationException extends RuntimeException {

    public BancsIntegrationException(String message) {
        super(message);
    }

    public BancsIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

