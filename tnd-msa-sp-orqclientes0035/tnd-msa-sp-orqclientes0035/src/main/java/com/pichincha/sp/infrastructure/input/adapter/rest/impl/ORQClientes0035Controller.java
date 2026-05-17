package com.pichincha.sp.infrastructure.input.adapter.rest.impl;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

import com.pichincha.sp.generated.BodyInCreacionCliente21;
import com.pichincha.sp.generated.BodyOutCreacionCliente21;
import com.pichincha.sp.generated.Field;
import com.pichincha.sp.generated.Flow;
import com.pichincha.sp.generated.GenericFlow;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapEnvelopeRequestDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapEnvelopeResponseDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapBodyResponseDto;

/**
 * Controlador REST para ORQClientes0035.
 * Implementa arquitectura hexagonal.
 * Utiliza clases genéricas de SOAP Envelope para manejar el envolvente SOAP.
 * 
 * Operaciones disponibles:
 * - CreacionCliente21: bp:CreacionCliente21Request -> bp:CreacionCliente21Response
 */
@RestController
@RequestMapping("/IntegrationBus/soap/ORQClientes0035")
public class ORQClientes0035Controller {

    /**
     * Operación SOAP: CreacionCliente21
     * 
     * @param soapRequest Petición SOAP completa con Envelope
     * @return Respuesta SOAP completa con Envelope
     */
    @PostMapping(
            consumes = {MediaType.TEXT_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.TEXT_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_XML_VALUE}
    )
    public Mono<SoapEnvelopeResponseDto> creacionCliente21(@RequestBody @Valid SoapEnvelopeRequestDto soapRequest) {
        BodyInCreacionCliente21 requestBody = soapRequest != null && soapRequest.getBody() != null
                ? soapRequest.getBody().getCreacionCliente21()
                : null;

        BodyOutCreacionCliente21 responseBody = new BodyOutCreacionCliente21();
        responseBody.setFlujo(mapFlujoWithCodigoEsql(requestBody != null ? requestBody.getFlujo() : null));

        SoapBodyResponseDto soapBodyResponseDto = new SoapBodyResponseDto();
        soapBodyResponseDto.setCreacionCliente21Response(responseBody);

        SoapEnvelopeResponseDto response = SoapEnvelopeResponseDto.builder()
                .body(soapBodyResponseDto)
                .build();
        
        return Mono.just(response);
    }

    private GenericFlow mapFlujoWithCodigoEsql(GenericFlow requestFlujo) {
        GenericFlow responseFlujo = new GenericFlow();
        Flow responseCampos = new Flow();

        if (requestFlujo != null) {
            responseFlujo.setEstado(requestFlujo.getEstado());
            if (requestFlujo.getCampos() != null && requestFlujo.getCampos().getCampos() != null) {
                responseCampos.getCampos().addAll(requestFlujo.getCampos().getCampos());
            }
        }

        Field codigoEsqlField = new Field();
        codigoEsqlField.setClave("codigoEsql");
        codigoEsqlField.setValor(obtenerValorCodigoEsql(responseCampos));
        responseCampos.getCampos().add(codigoEsqlField);

        responseFlujo.setCampos(responseCampos);
        return responseFlujo;
    }

    private String obtenerValorCodigoEsql(Flow campos) {
        if (campos == null || campos.getCampos() == null) {
            return "";
        }

        for (Field campo : campos.getCampos()) {
            if (campo != null && campo.getClave() != null
                    && ("codigoEsql".equalsIgnoreCase(campo.getClave())
                    || "codigo".equalsIgnoreCase(campo.getClave()))) {
                return campo.getValor() != null ? campo.getValor() : "";
            }
        }

        return "";
    }
}
