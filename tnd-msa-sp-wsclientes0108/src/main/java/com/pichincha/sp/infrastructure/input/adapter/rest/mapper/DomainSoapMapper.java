package com.pichincha.sp.infrastructure.input.adapter.rest.mapper;

import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapBodyResponse;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapEnvelopeResponse;
import org.mapstruct.Mapper;

/**
 * Mapper: Domain Model → SOAP Response
 * Responsabilidad única: mapeo de resultado de dominio a respuesta SOAP.
 * Interfaz segregada (ISP) para escritura/transformación de salida.
 */
@Mapper(componentModel = "spring")
public interface DomainSoapMapper {

    /**
     * Convierte resultado de dominio a envelope SOAP.
     */
    default SoapEnvelopeResponse toSoapResponse(CreateCustomerResultDto result) {
        com.pichincha.sp.generated.CrearCliente24Response operationResponse = new com.pichincha.sp.generated.CrearCliente24Response();
        if (result != null) {
            com.pichincha.sp.generated.CrearCliente24Response.BodyOut bodyOut = operationResponse.getBodyOut();
            if (bodyOut == null) {
                bodyOut = new com.pichincha.sp.generated.CrearCliente24Response.BodyOut();
                operationResponse.setBodyOut(bodyOut);
            }

            com.pichincha.sp.generated.CrearCliente24Response.BodyOut.Error error = bodyOut.getError();
            if (error == null) {
                error = new com.pichincha.sp.generated.CrearCliente24Response.BodyOut.Error();
                bodyOut.setError(error);
            }

            error.setCodigo(result.getCode());
            error.setMensaje(result.getMessage());
            error.setTipo(result.getType());
        }

        return SoapEnvelopeResponse.builder()
                .body(SoapBodyResponse.builder().crearCliente24Response(operationResponse).build())
                .build();
    }
}

