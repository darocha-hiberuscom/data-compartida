package com.pichincha.sp.infrastructure.input.adapter.soap.dto.response;

import com.pichincha.sp.generated.CrearCliente24Response;
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
public class SoapBodyResponse {

    @XmlElement(name = "crearCliente24Response", namespace = "http://bpichincha.com/servicios")
    private CrearCliente24Response crearCliente24Response;
}
