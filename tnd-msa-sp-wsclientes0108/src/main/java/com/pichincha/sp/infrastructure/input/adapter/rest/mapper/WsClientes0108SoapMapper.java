package com.pichincha.sp.infrastructure.input.adapter.rest.mapper;

import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.generated.CrearCliente24;
import com.pichincha.sp.generated.CrearCliente24Response;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.request.SoapEnvelopeRequest;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapBodyResponse;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapEnvelopeResponse;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class WsClientes0108SoapMapper {

    public CreateCustomerInputDto toDomain(SoapEnvelopeRequest request) {
        CrearCliente24 operation = Optional.ofNullable(request)
                .map(SoapEnvelopeRequest::getBody)
                .map(body -> body.getCrearCliente24())
                .orElse(null);

        return CreateCustomerInputDto.builder()
                .customerIdentifier(readString(operation, "getBodyIn", "getCif"))
                .primaryLastName(readString(operation, "getBodyIn", "getDatosBasicos", "getApellidoPrincipal"))
                .secondaryLastName(readString(operation, "getBodyIn", "getDatosBasicos", "getApellidoSecundario"))
                .primaryName(readString(operation, "getBodyIn", "getDatosBasicos", "getNombrePrincipal"))
                .secondaryName(readString(operation, "getBodyIn", "getDatosBasicos", "getNombreSecundario"))
                .customerStatus(readString(operation, "getBodyIn", "getDatosBasicos", "getEstadoCliente"))
                .birthDate(readString(operation, "getBodyIn", "getDatosAdicionales", "getFechaNacimiento"))
                .birthPlace(readString(operation, "getBodyIn", "getDatosAdicionales", "getLugarNacimiento"))
                .gender(readString(operation, "getBodyIn", "getDatosAdicionales", "getGenero"))
                .maritalStatus(readString(operation, "getBodyIn", "getDatosAdicionales", "getEstadoCivil"))
                .workPlace(readString(operation, "getBodyIn", "getDatosAdicionales", "getLugarTrabajo"))
                .spouseName(readString(operation, "getBodyIn", "getDatosAdicionales", "getNombreApellidoConyuge"))
                .age(readString(operation, "getBodyIn", "getDatosAdicionales", "getEdad"))
                .documentedCondition(readString(operation, "getBodyIn", "getDatosAdicionales", "getCondicionCedulado"))
                .customerCreationReason(readString(operation, "getBodyIn", "getDatosAdicionales", "getMotivoCreacionCliente"))
                .accountingRequired(readString(operation, "getBodyIn", "getDatosAdicionales", "getClienteLlevaContabilidad"))
                .otherNationality(readString(operation, "getBodyIn", "getFatca", "getOtraNacionalidad"))
                .tinCode(readString(operation, "getBodyIn", "getFatca", "getCodigoTIN"))
                .usLegalRepresentative(readString(operation, "getBodyIn", "getFatca", "getCuentaConRepresentanteLegalEEUU"))
                .externalTaxObligations(readString(operation, "getBodyIn", "getFatca", "getObligacionesTributariasExterior"))
                .externalTaxCountry(readString(operation, "getBodyIn", "getFatca", "getPaisObligacionesTributariasExterior"))
                .nationalityTwo(readString(operation, "getBodyIn", "getFatca", "getNacionalidad2"))
                .nationalityThree(readString(operation, "getBodyIn", "getFatca", "getNacionalidad3"))
                .correspondenceAddress(readString(operation, "getBodyIn", "getFatca", "getDireccionCorrespondencia"))
                .build();
    }

    public SoapEnvelopeResponse toSoapResponse(CreateCustomerResultDto result) {
        CrearCliente24Response operationResponse = new CrearCliente24Response();
        return SoapEnvelopeResponse.builder()
                .body(SoapBodyResponse.builder().crearCliente24Response(operationResponse).build())
                .build();
    }

    private String readString(Object source, String... methodPath) {
        try {
            Object current = source;
            for (String methodName : methodPath) {
                if (current == null) {
                    return null;
                }
                Method method = current.getClass().getMethod(methodName);
                current = method.invoke(current);
            }
            return current == null ? null : String.valueOf(current);
        } catch (Exception exception) {
            return null;
        }
    }
}
