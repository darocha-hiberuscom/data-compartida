package com.pichincha.sp.infrastructure.exception.error.resolver;

import com.pichincha.sp.infrastructure.exception.ErrorCatalogConstants;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultBodyDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultEnvelopeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class UnexpectedErrorResolver extends ErrorResolver<Throwable> {

    private static final String TITLE_UNEXPECTED_ERROR = "Unexpected error";
    private static final String DETAIL_UNEXPECTED_ERROR =
            "Detailed error information: An unexpected error has occurred";

    @NonNull
    @Override
    protected SoapFaultEnvelopeDto buildError(@NonNull ServerHttpResponse serverResponse,
                                               @NonNull final String requestPath,
                                               @NonNull final Throwable throwable) {
        log.error(TITLE_UNEXPECTED_ERROR, throwable);
        serverResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        SoapFaultDto faultDetail = getFaultDetail();
        SoapFaultBodyDto faultBody = new SoapFaultBodyDto();
        faultBody.setFault(faultDetail);
        SoapFaultEnvelopeDto soapFault = new SoapFaultEnvelopeDto();
        soapFault.setBody(faultBody);
        return soapFault;
    }

    private static SoapFaultDto getFaultDetail() {
        SoapFaultDto faultDetail = new SoapFaultDto();
        faultDetail.setFaultCode(ErrorCatalogConstants.FAULT_CODE);
        faultDetail.setFaultString(TITLE_UNEXPECTED_ERROR);
        faultDetail.setDetail(DETAIL_UNEXPECTED_ERROR);
        return faultDetail;
    }
}
