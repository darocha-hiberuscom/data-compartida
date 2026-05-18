package com.pichincha.sp.infrastructure.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCatalogConstants {

    // Fields
    public static final String FAULT_CODE = "soapenv:Client";
    public static final String FAULT_ACTOR = "http://pichincha.com/wsdl/Service";
    public static final String EXCEPTION_TITLE = "Service exception";
    public static final String DETAILED_ERROR_INFORMATION = "Detailed error information:";
}
