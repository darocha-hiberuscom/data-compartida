@XmlSchema(
    xmlns = {
        @XmlNs(prefix = "soapenv", namespaceURI = "http://schemas.xmlsoap.org/soap/envelope/"),
        @XmlNs(prefix = "ns2", namespaceURI = "http://bpichincha.com/servicios")
    }
)
package com.pichincha.sp.infrastructure.input.adapter.rest.dto;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlSchema;