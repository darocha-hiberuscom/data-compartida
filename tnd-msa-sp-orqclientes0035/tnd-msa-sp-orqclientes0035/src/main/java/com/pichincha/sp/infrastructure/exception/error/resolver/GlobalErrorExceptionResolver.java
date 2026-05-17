package com.pichincha.sp.infrastructure.exception.error.resolver;

import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.exception.ErrorCatalogConstants;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultBodyDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultEnvelopeDto;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class GlobalErrorExceptionResolver extends ErrorResolver<GlobalErrorException> {

    @Override
    protected SoapFaultEnvelopeDto buildError(@NonNull ServerHttpResponse serverResponse,
                                               @NonNull String requestPath,
                                               @NonNull GlobalErrorException throwable) {
        serverResponse.setStatusCode(throwable.getStatusCode());
        SoapFaultDto faultDetail = getFaultDetail(throwable);
        SoapFaultBodyDto faultBody = new SoapFaultBodyDto();
        faultBody.setFault(faultDetail);
        SoapFaultEnvelopeDto soapFault = new SoapFaultEnvelopeDto();
        soapFault.setBody(faultBody);
        return soapFault;
    }

    private static SoapFaultDto getFaultDetail(GlobalErrorException throwable) {
        SoapFaultDto faultDetail = new SoapFaultDto();
        faultDetail.setFaultCode(ErrorCatalogConstants.FAULT_CODE);
        faultDetail.setFaultString(ErrorCatalogConstants.EXCEPTION_TITLE);
        faultDetail.setDetail(ErrorCatalogConstants.DETAILED_ERROR_INFORMATION + throwable.getMessage());
        return faultDetail;
    }
}
