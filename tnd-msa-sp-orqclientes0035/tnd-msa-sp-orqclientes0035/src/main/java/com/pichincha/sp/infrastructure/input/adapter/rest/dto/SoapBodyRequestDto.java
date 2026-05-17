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

import com.pichincha.sp.generated.BodyInCreacionCliente21;

/**
 * Body del SOAP Envelope para requests
 * Contiene los elementos de las operaciones generadas desde el WSDL
 * 
 * Operaciones soportadas:
 * - CreacionCliente21
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapBodyRequestDto {

    @XmlElement(name = "CreacionCliente21", namespace = "http://bpichincha.com/servicios")
    @Valid
    private BodyInCreacionCliente21 CreacionCliente21;
}
