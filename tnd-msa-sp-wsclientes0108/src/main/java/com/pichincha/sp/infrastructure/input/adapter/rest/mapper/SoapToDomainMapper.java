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
public interface SoapToDomainMapper {

    @Mapping(target = "customerIdentifier", source = "body.crearCliente24.bodyIn.cif")
    @Mapping(target = "primaryLastName", source = "body.crearCliente24.bodyIn.datosBasicos.apellidoPrincipal")
    @Mapping(target = "secondaryLastName", source = "body.crearCliente24.bodyIn.datosBasicos.apellidoSecundario")
    @Mapping(target = "primaryName", source = "body.crearCliente24.bodyIn.datosBasicos.nombrePrincipal")
    @Mapping(target = "secondaryName", source = "body.crearCliente24.bodyIn.datosBasicos.nombreSecundario")
    @Mapping(target = "customerStatus", source = "body.crearCliente24.bodyIn.datosBasicos.estadoCliente")
    @Mapping(target = "birthDate", source = "body.crearCliente24.bodyIn.datosAdicionales.fechaNacimiento")
    @Mapping(target = "birthPlace", source = "body.crearCliente24.bodyIn.datosAdicionales.lugarNacimiento")
    @Mapping(target = "gender", source = "body.crearCliente24.bodyIn.datosAdicionales.genero")
    @Mapping(target = "maritalStatus", source = "body.crearCliente24.bodyIn.datosAdicionales.estadoCivil")
    @Mapping(target = "workPlace", source = "body.crearCliente24.bodyIn.datosAdicionales.lugarTrabajo")
    @Mapping(target = "spouseName", source = "body.crearCliente24.bodyIn.datosAdicionales.nombreApellidoConyuge")
    @Mapping(target = "age", source = "body.crearCliente24.bodyIn.datosAdicionales.edad")
    @Mapping(target = "documentedCondition", source = "body.crearCliente24.bodyIn.datosAdicionales.condicionCedulado")
    @Mapping(target = "customerCreationReason", source = "body.crearCliente24.bodyIn.datosAdicionales.motivoCreacionCliente")
    @Mapping(target = "accountingRequired", source = "body.crearCliente24.bodyIn.datosAdicionales.clienteLlevaContabilidad")
    @Mapping(target = "otherNationality", source = "body.crearCliente24.bodyIn.fatca.otraNacionalidad")
    @Mapping(target = "tinCode", source = "body.crearCliente24.bodyIn.fatca.codigoTIN")
    @Mapping(target = "usLegalRepresentative", source = "body.crearCliente24.bodyIn.fatca.cuentaConRepresentanteLegalEEUU")
    @Mapping(target = "externalTaxObligations", source = "body.crearCliente24.bodyIn.fatca.obligacionesTributariasExterior")
    @Mapping(target = "externalTaxCountry", source = "body.crearCliente24.bodyIn.fatca.paisObligacionesTributariasExterior")
    @Mapping(target = "nationalityTwo", source = "body.crearCliente24.bodyIn.fatca.nacionalidad2")
    @Mapping(target = "nationalityThree", source = "body.crearCliente24.bodyIn.fatca.nacionalidad3")
    @Mapping(target = "correspondenceAddress", source = "body.crearCliente24.bodyIn.fatca.direccionCorrespondencia")
    CreateCustomerInputDto toDomain(SoapEnvelopeRequest request);
}
