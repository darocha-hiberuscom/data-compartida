package com.pichincha.sp.infrastructure.input.adapter.rest.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.pichincha.sp.generated.BodyOutCreacionCliente21;

/**
 * Body del SOAP Envelope para responses
 * Contiene los elementos de respuesta de las operaciones generadas desde el WSDL
 * 
 * Operaciones soportadas:
 * - CreacionCliente21Response
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapBodyResponseDto {

    @XmlElement(name = "CreacionCliente21Response", namespace = "http://bpichincha.com/servicios")
    private BodyOutCreacionCliente21 CreacionCliente21Response;
}
