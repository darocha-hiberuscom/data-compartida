package com.pichincha.sp.infrastructure.input.adapter.rest.translator;

import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.generated.CrearCliente24;
import com.pichincha.sp.generated.CrearCliente24Response;
import com.pichincha.sp.infrastructure.input.adapter.rest.mapper.WsClientes0108SoapDomainMapper;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.request.SoapEnvelopeRequest;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapBodyResponse;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapEnvelopeResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Traductor SOAP que orquesta la conversión SOAP ↔ Domain.
 * Utiliza el mapper MapStruct para mapeos declarativos.
 * NO es un mapper, es un adaptador/orquestador.
 */
@Component
public class WsClientes0108SoapTranslator {

    private final WsClientes0108SoapDomainMapper soapDomainMapper;

    public WsClientes0108SoapTranslator(WsClientes0108SoapDomainMapper soapDomainMapper) {
        this.soapDomainMapper = soapDomainMapper;
    }

    public CreateCustomerInputDto toDomain(SoapEnvelopeRequest request) {
        CrearCliente24 operation = Optional.ofNullable(request)
                .map(SoapEnvelopeRequest::getBody)
                .map(body -> body.getCrearCliente24())
                .orElse(null);

        if (operation == null) {
            return CreateCustomerInputDto.builder().build();
        }

        return soapDomainMapper.toDomain(operation);
    }

    public SoapEnvelopeResponse toSoapResponse(CreateCustomerResultDto result) {
        CrearCliente24Response operationResponse = new CrearCliente24Response();
        populateResultError(operationResponse, result);

        return SoapEnvelopeResponse.builder()
                .body(SoapBodyResponse.builder().crearCliente24Response(operationResponse).build())
                .build();
    }

    private void populateResultError(CrearCliente24Response response, CreateCustomerResultDto result) {
        if (result == null || response == null) {
            return;
        }

        CrearCliente24Response.BodyOut bodyOut = response.getBodyOut();
        if (bodyOut == null) {
            bodyOut = new CrearCliente24Response.BodyOut();
            response.setBodyOut(bodyOut);
        }

        CrearCliente24Response.BodyOut.Error error = bodyOut.getError();
        if (error == null) {
            error = new CrearCliente24Response.BodyOut.Error();
            bodyOut.setError(error);
        }

        error.setCodigo(result.getCode());
        error.setMensaje(result.getMessage());
        error.setTipo(result.getType());
    }
}

