package com.pichincha.sp.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalErrorException extends RuntimeException {

    protected final HttpStatus statusCode;
    protected final String message;
    protected final String component;

    public GlobalErrorException(String message, String component, HttpStatus status) {
        super(message);
        this.statusCode = status;
        this.message = message;
        this.component = component;
    }
}
