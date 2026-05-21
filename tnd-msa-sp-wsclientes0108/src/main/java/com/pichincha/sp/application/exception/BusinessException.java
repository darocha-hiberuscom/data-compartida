package com.pichincha.sp.application.exception;

import com.pichincha.sp.domain.dto.BusinessErrorDto;

public class BusinessException extends RuntimeException {

    private final BusinessErrorDto businessError;

    public BusinessException(BusinessErrorDto businessError) {
        super(businessError != null ? businessError.getMessage() : null);
        this.businessError = businessError;
    }

    public BusinessException(BusinessErrorDto businessError, Throwable cause) {
        super(businessError != null ? businessError.getMessage() : null, cause);
        this.businessError = businessError;
    }

    public BusinessErrorDto getBusinessError() {
        return businessError;
    }
}

