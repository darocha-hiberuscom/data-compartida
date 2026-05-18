package com.pichincha.sp.infrastructure.input.adapter.rest.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.pichincha.sp.generated.CrearCliente24Response;

/**
 * Body del SOAP Envelope para responses
 * Contiene los elementos de respuesta de las operaciones generadas desde el WSDL
 * 
 * Operaciones soportadas:
 * - crearCliente24Response
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapBodyResponseDto {

    @XmlElement(name = "crearCliente24Response", namespace = "http://bpichincha.com/servicios")
    private CrearCliente24Response crearCliente24Response;
}
