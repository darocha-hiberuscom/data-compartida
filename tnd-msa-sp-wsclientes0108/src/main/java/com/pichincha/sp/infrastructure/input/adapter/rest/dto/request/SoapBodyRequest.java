package com.pichincha.sp.infrastructure.input.adapter.soap.dto.request;

import com.pichincha.sp.generated.CrearCliente24;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapBodyRequest {

    @Valid
    @XmlElement(name = "crearCliente24", namespace = "http://bpichincha.com/servicios")
    private CrearCliente24 crearCliente24;
}
