package com.pichincha.sp.infrastructure.exception.error.resolver;

import com.pichincha.sp.infrastructure.exception.ErrorCatalogConstants;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultBodyDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultEnvelopeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public final class ResponseStatusExceptionResolver extends ErrorResolver<ResponseStatusException> {

    @Override
    protected SoapFaultEnvelopeDto buildError(@NonNull ServerHttpResponse serverResponse,
                                               @NonNull String requestPath,
                                               @NonNull ResponseStatusException exception) {
        serverResponse.setStatusCode(HttpStatus.valueOf(exception.getBody().getStatus()));
        return buildBaseErrorModel(exception.getBody());
    }

    @NonNull
    public static SoapFaultEnvelopeDto buildBaseErrorModel(ProblemDetail body) {
        SoapFaultDto faultDetail = getFaultDetail(body);
        SoapFaultBodyDto faultBody = new SoapFaultBodyDto();
        faultBody.setFault(faultDetail);
        SoapFaultEnvelopeDto soapFault = new SoapFaultEnvelopeDto();
        soapFault.setBody(faultBody);
        return soapFault;
    }

    private static SoapFaultDto getFaultDetail(ProblemDetail body) {
        SoapFaultDto faultDetail = new SoapFaultDto();
        faultDetail.setFaultCode(ErrorCatalogConstants.FAULT_CODE);
        faultDetail.setFaultString(body.getTitle());
        faultDetail.setDetail(ErrorCatalogConstants.DETAILED_ERROR_INFORMATION + body.getDetail());
        return faultDetail;
    }
}
