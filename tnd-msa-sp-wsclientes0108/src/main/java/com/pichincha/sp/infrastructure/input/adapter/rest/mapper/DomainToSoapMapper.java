package com.pichincha.sp.infrastructure.input.adapter.rest.mapper;

import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.generated.CrearCliente24Response;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapEnvelopeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper: Domain Model → SOAP Response
 * Responsabilidad única: mapeo de resultado de dominio a respuesta SOAP.
 * Interfaz segregada (ISP) para escritura/transformación de salida.
 */
@Mapper(componentModel = "spring")
public interface DomainToSoapMapper {

    @Mapping(target = "bodyOut.error.codigo", source = "code")
    @Mapping(target = "bodyOut.error.mensaje", source = "message")
    @Mapping(target = "bodyOut.error.tipo", source = "type")
    CrearCliente24Response toOperationResponse(CreateCustomerResultDto result);

    @Mapping(target = "body.crearCliente24Response", source = "operationResponse")
    SoapEnvelopeResponse toEnvelope(CrearCliente24Response operationResponse);

    default SoapEnvelopeResponse toSoap(CreateCustomerResultDto result) {
        return toEnvelope(toOperationResponse(result));
    }
}
