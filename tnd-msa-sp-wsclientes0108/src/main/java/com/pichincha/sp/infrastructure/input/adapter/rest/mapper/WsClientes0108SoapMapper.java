package com.pichincha.sp.infrastructure.input.adapter.rest.mapper;

import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.generated.CrearCliente24;
import com.pichincha.sp.generated.CrearCliente24Response;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.request.SoapEnvelopeRequest;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapBodyResponse;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapEnvelopeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WsClientes0108SoapMapper {

    @Mapping(target = "customerIdentifier", expression = "java(readCif(request))")
    @Mapping(target = "primaryLastName", expression = "java(readPrimaryLastName(request))")
    @Mapping(target = "secondaryLastName", expression = "java(readSecondaryLastName(request))")
    @Mapping(target = "primaryName", expression = "java(readPrimaryName(request))")
    @Mapping(target = "secondaryName", expression = "java(readSecondaryName(request))")
    @Mapping(target = "customerStatus", expression = "java(readCustomerStatus(request))")
    @Mapping(target = "birthDate", expression = "java(readBirthDate(request))")
    @Mapping(target = "birthPlace", expression = "java(readBirthPlace(request))")
    @Mapping(target = "gender", expression = "java(readGender(request))")
    @Mapping(target = "maritalStatus", expression = "java(readMaritalStatus(request))")
    @Mapping(target = "workPlace", expression = "java(readWorkPlace(request))")
    @Mapping(target = "spouseName", expression = "java(readSpouseName(request))")
    @Mapping(target = "age", expression = "java(readAge(request))")
    @Mapping(target = "documentedCondition", expression = "java(readDocumentedCondition(request))")
    @Mapping(target = "customerCreationReason", expression = "java(readCustomerCreationReason(request))")
    @Mapping(target = "accountingRequired", expression = "java(readAccountingRequired(request))")
    @Mapping(target = "otherNationality", expression = "java(readOtherNationality(request))")
    @Mapping(target = "tinCode", expression = "java(readTinCode(request))")
    @Mapping(target = "usLegalRepresentative", expression = "java(readUsLegalRepresentative(request))")
    @Mapping(target = "externalTaxObligations", expression = "java(readExternalTaxObligations(request))")
    @Mapping(target = "externalTaxCountry", expression = "java(readExternalTaxCountry(request))")
    @Mapping(target = "nationalityTwo", expression = "java(readNationalityTwo(request))")
    @Mapping(target = "nationalityThree", expression = "java(readNationalityThree(request))")
    @Mapping(target = "correspondenceAddress", expression = "java(readCorrespondenceAddress(request))")
    CreateCustomerInputDto toDomain(SoapEnvelopeRequest request);

    default SoapEnvelopeResponse toSoapResponse(CreateCustomerResultDto result) {
        CrearCliente24Response operationResponse = new CrearCliente24Response();
        if (result != null) {
            CrearCliente24Response.BodyOut bodyOut = operationResponse.getBodyOut();
            if (bodyOut == null) {
                bodyOut = new CrearCliente24Response.BodyOut();
                operationResponse.setBodyOut(bodyOut);
            }

            CrearCliente24Response.BodyOut.Error error = bodyOut.getError();
            if (error == null) {
                error = new CrearCliente24Response.BodyOut.Error();
                bodyOut.setError(error);
            }

            error.setCodigo(result.getCode());
            error.setMensaje(result.getMessage());
            error.setTipo(result.getType());
        }

        return SoapEnvelopeResponse.builder()
                .body(SoapBodyResponse.builder().crearCliente24Response(operationResponse).build())
                .build();
    }

    default CrearCliente24 operation(SoapEnvelopeRequest request) {
        if (request == null || request.getBody() == null) {
            return null;
        }
        return request.getBody().getCrearCliente24();
    }

    default CrearCliente24.BodyIn bodyIn(SoapEnvelopeRequest request) {
        CrearCliente24 operation = operation(request);
        return operation == null ? null : operation.getBodyIn();
    }

    default CrearCliente24.BodyIn.DatosBasicos datosBasicos(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn bodyIn = bodyIn(request);
        return bodyIn == null ? null : bodyIn.getDatosBasicos();
    }

    default CrearCliente24.BodyIn.DatosAdicionales datosAdicionales(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn bodyIn = bodyIn(request);
        return bodyIn == null ? null : bodyIn.getDatosAdicionales();
    }

    default CrearCliente24.BodyIn.Fatca fatca(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn bodyIn = bodyIn(request);
        return bodyIn == null ? null : bodyIn.getFatca();
    }

    default String readCif(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn value = bodyIn(request);
        return value == null ? null : value.getCif();
    }

    default String readPrimaryLastName(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getApellidoPrincipal();
    }

    default String readSecondaryLastName(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getApellidoSecundario();
    }

    default String readPrimaryName(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getNombrePrincipal();
    }

    default String readSecondaryName(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getNombreSecundario();
    }

    default String readCustomerStatus(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getEstadoCliente();
    }

    default String readBirthDate(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getFechaNacimiento();
    }

    default String readBirthPlace(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getLugarNacimiento();
    }

    default String readGender(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getGenero();
    }

    default String readMaritalStatus(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getEstadoCivil();
    }

    default String readWorkPlace(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getLugarTrabajo();
    }

    default String readSpouseName(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getNombreApellidoConyuge();
    }

    default String readAge(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getEdad();
    }

    default String readDocumentedCondition(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getCondicionCedulado();
    }

    default String readCustomerCreationReason(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getMotivoCreacionCliente();
    }

    default String readAccountingRequired(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getClienteLlevaContabilidad();
    }

    default String readOtherNationality(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getOtraNacionalidad();
    }

    default String readTinCode(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getCodigoTIN();
    }

    default String readUsLegalRepresentative(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getCuentaConRepresentanteLegalEEUU();
    }

    default String readExternalTaxObligations(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getObligacionesTributariasExterior();
    }

    default String readExternalTaxCountry(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getPaisObligacionesTributariasExterior();
    }

    default String readNationalityTwo(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getNacionalidad2();
    }

    default String readNationalityThree(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getNacionalidad3();
    }

    default String readCorrespondenceAddress(SoapEnvelopeRequest request) {
        CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getDireccionCorrespondencia();
    }
}

