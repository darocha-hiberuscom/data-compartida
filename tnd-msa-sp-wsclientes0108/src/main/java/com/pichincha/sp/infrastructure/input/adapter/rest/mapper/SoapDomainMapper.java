package com.pichincha.sp.infrastructure.input.adapter.rest.mapper;

import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.request.SoapEnvelopeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper: SOAP Request → Domain Model
 * Responsabilidad única: mapeo de entrada SOAP al modelo de dominio.
 * Interfaz segregada (ISP) para lectura/conversión de entrada.
 */
@Mapper(componentModel = "spring")
public interface SoapDomainMapper {

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

    // ...helper methods...
    default com.pichincha.sp.generated.CrearCliente24 operation(SoapEnvelopeRequest request) {
        if (request == null || request.getBody() == null) {
            return null;
        }
        return request.getBody().getCrearCliente24();
    }

    default com.pichincha.sp.generated.CrearCliente24.BodyIn bodyIn(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24 operation = operation(request);
        return operation == null ? null : operation.getBodyIn();
    }

    default com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosBasicos datosBasicos(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn bodyIn = bodyIn(request);
        return bodyIn == null ? null : bodyIn.getDatosBasicos();
    }

    default com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales datosAdicionales(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn bodyIn = bodyIn(request);
        return bodyIn == null ? null : bodyIn.getDatosAdicionales();
    }

    default com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca fatca(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn bodyIn = bodyIn(request);
        return bodyIn == null ? null : bodyIn.getFatca();
    }

    default String readCif(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn value = bodyIn(request);
        return value == null ? null : value.getCif();
    }

    default String readPrimaryLastName(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getApellidoPrincipal();
    }

    default String readSecondaryLastName(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getApellidoSecundario();
    }

    default String readPrimaryName(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getNombrePrincipal();
    }

    default String readSecondaryName(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getNombreSecundario();
    }

    default String readCustomerStatus(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosBasicos value = datosBasicos(request);
        return value == null ? null : value.getEstadoCliente();
    }

    default String readBirthDate(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getFechaNacimiento();
    }

    default String readBirthPlace(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getLugarNacimiento();
    }

    default String readGender(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getGenero();
    }

    default String readMaritalStatus(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getEstadoCivil();
    }

    default String readWorkPlace(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getLugarTrabajo();
    }

    default String readSpouseName(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getNombreApellidoConyuge();
    }

    default String readAge(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getEdad();
    }

    default String readDocumentedCondition(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getCondicionCedulado();
    }

    default String readCustomerCreationReason(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getMotivoCreacionCliente();
    }

    default String readAccountingRequired(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.DatosAdicionales value = datosAdicionales(request);
        return value == null ? null : value.getClienteLlevaContabilidad();
    }

    default String readOtherNationality(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getOtraNacionalidad();
    }

    default String readTinCode(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getCodigoTIN();
    }

    default String readUsLegalRepresentative(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getCuentaConRepresentanteLegalEEUU();
    }

    default String readExternalTaxObligations(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getObligacionesTributariasExterior();
    }

    default String readExternalTaxCountry(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getPaisObligacionesTributariasExterior();
    }

    default String readNationalityTwo(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getNacionalidad2();
    }

    default String readNationalityThree(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getNacionalidad3();
    }

    default String readCorrespondenceAddress(SoapEnvelopeRequest request) {
        com.pichincha.sp.generated.CrearCliente24.BodyIn.Fatca value = fatca(request);
        return value == null ? null : value.getDireccionCorrespondencia();
    }
}

