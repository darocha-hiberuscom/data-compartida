package com.pichincha.sp.infrastructure.exception.error.resolver;

import com.pichincha.sp.application.exception.BancsIntegrationException;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultBodyDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultEnvelopeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class BancsIntegrationExceptionResolver extends ErrorResolver<BancsIntegrationException> {

    private static final String BANCS_ERROR_CODE = "9928";
    private static final String BANCS_ERROR_TYPE = "FATAL";

    @Override
    protected SoapFaultEnvelopeDto buildError(@NonNull ServerHttpResponse serverResponse,
                                               @NonNull String requestPath,
                                               @NonNull BancsIntegrationException throwable) {
        serverResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        SoapFaultDto faultDetail = new SoapFaultDto();
        faultDetail.setFaultCode(BANCS_ERROR_CODE);
        faultDetail.setFaultString("Error de integración con BANCS");
        faultDetail.setDetail(buildDetailMessage(throwable, requestPath));

        SoapFaultBodyDto faultBody = new SoapFaultBodyDto();
        faultBody.setFault(faultDetail);

        SoapFaultEnvelopeDto soapFault = new SoapFaultEnvelopeDto();
        soapFault.setBody(faultBody);

        return soapFault;
    }

    private String buildDetailMessage(BancsIntegrationException throwable, String requestPath) {
        String message = throwable.getMessage() != null ? throwable.getMessage() : "Error desconocido";
        String cause = throwable.getCause() != null ? throwable.getCause().getMessage() : "";

        return String.format(
                "Tipo: %s | Ruta: %s | Mensaje: %s | Causa: %s",
                BANCS_ERROR_TYPE,
                requestPath,
                message,
                cause
        );
    }
}

