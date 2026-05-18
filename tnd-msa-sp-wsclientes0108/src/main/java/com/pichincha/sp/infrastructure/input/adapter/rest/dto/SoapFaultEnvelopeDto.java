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
 * Represents a SOAP Envelope containing a Fault according to SOAP 1.1 standard.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapFaultEnvelopeDto {

    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    private SoapFaultBodyDto body;

    /**
     * Creates a SoapFaultEnvelopeDto wrapping the provided SoapFault.
     *
     * @param fault The SoapFaultDto to wrap
     * @return A complete SoapFaultEnvelopeDto
     */
    public static SoapFaultEnvelopeDto withFault(SoapFaultDto fault) {
        return SoapFaultEnvelopeDto.builder()
                .body(SoapFaultBodyDto.builder()
                        .fault(fault)
                        .build())
                .build();
    }
}
