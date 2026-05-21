package com.pichincha.sp.infrastructure.exception.error.resolver;

import com.pichincha.sp.application.exception.BusinessException;
import com.pichincha.sp.domain.dto.BusinessErrorDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultBodyDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultEnvelopeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class BusinessExceptionResolver extends ErrorResolver<BusinessException> {

    @Override
    protected SoapFaultEnvelopeDto buildError(@NonNull ServerHttpResponse serverResponse,
                                               @NonNull String requestPath,
                                               @NonNull BusinessException throwable) {
        serverResponse.setStatusCode(HttpStatus.BAD_REQUEST);

        BusinessErrorDto businessError = throwable.getBusinessError();
        if (businessError == null) {
            return buildDefaultFault();
        }

        SoapFaultDto faultDetail = new SoapFaultDto();
        faultDetail.setFaultCode(businessError.getCode());
        faultDetail.setFaultString(businessError.getMessage());
        faultDetail.setDetail(buildDetailMessage(businessError));

        SoapFaultBodyDto faultBody = new SoapFaultBodyDto();
        faultBody.setFault(faultDetail);

        SoapFaultEnvelopeDto soapFault = new SoapFaultEnvelopeDto();
        soapFault.setBody(faultBody);

        return soapFault;
    }

    private String buildDetailMessage(BusinessErrorDto businessError) {
        return String.format(
                "Componente: %s | Recurso: %s | Backend: %s | Tipo: %s",
                businessError.getComponent(),
                businessError.getResource(),
                businessError.getBackend(),
                businessError.getType()
        );
    }

    private SoapFaultEnvelopeDto buildDefaultFault() {
        SoapFaultDto faultDetail = new SoapFaultDto();
        faultDetail.setFaultCode("9999");
        faultDetail.setFaultString("Error de negocio sin detalles disponibles");
        faultDetail.setDetail("Por favor contacte al administrador del sistema");

        SoapFaultBodyDto faultBody = new SoapFaultBodyDto();
        faultBody.setFault(faultDetail);

        SoapFaultEnvelopeDto soapFault = new SoapFaultEnvelopeDto();
        soapFault.setBody(faultBody);

        return soapFault;
    }
}

