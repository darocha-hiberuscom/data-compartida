package com.pichincha.sp.infrastructure.input.adapter.rest.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a SOAP Fault structure according to SOAP 1.1 standard.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Fault", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapFaultDto {

    @XmlElement(name = "faultcode", namespace = "")
    private String faultCode;

    @XmlElement(name = "faultstring", namespace = "")
    private String faultString;

    @XmlElement(name = "detail", namespace = "")
    private String detail;
}
