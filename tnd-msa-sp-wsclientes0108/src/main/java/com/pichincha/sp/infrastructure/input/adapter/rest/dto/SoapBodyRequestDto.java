package com.pichincha.sp.infrastructure.input.adapter.rest.dto;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.pichincha.sp.generated.CrearCliente24;

/**
 * Body del SOAP Envelope para requests
 * Contiene los elementos de las operaciones generadas desde el WSDL
 * 
 * Operaciones soportadas:
 * - crearCliente24
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapBodyRequestDto {

    @XmlElement(name = "crearCliente24", namespace = "http://bpichincha.com/servicios")
    @Valid
    private CrearCliente24 crearCliente24;
}
