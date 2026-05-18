package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateResponseDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateResponseDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateResponseDto;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupResponseDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BancsDomainMapper {

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "defInteger1", source = "operationOption")
    Tx067050TechnicalRequest toTx067050Request(CustomerLookupRequestDto request);

    @Mapping(target = "customerIdentifier", source = "accntNumber1")
    @Mapping(target = "payloadReference", source = "accntNumber1")
    CustomerLookupResponseDto toLookupResponse(Tx067050TechnicalResponse response);

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "defInteger4", source = "customerStatus")
    @Mapping(target = "defaultString2", source = "primaryLastName")
    @Mapping(target = "name1", source = "primaryName")
    @Mapping(target = "defaultString3", source = "secondaryLastName")
    @Mapping(target = "name2", source = "secondaryName")
    Tx067000TechnicalRequest toTx067000Request(CustomerBasicDataUpdateRequestDto request);

    CustomerBasicDataUpdateResponseDto toBasicResponse(Tx067000TechnicalResponse response);

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "date1", source = "birthDate")
    @Mapping(target = "defaultString8", source = "birthPlace")
    @Mapping(target = "defaultString2", source = "gender")
    @Mapping(target = "defaultString3", source = "maritalStatus")
    @Mapping(target = "defaultString5", source = "workPlace")
    Tx067101TechnicalRequest toTx067101Request(CustomerPersonalDataUpdateRequestDto request);

    CustomerPersonalDataUpdateResponseDto toPersonalResponse(Tx067101TechnicalResponse response);

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "custAge", source = "age")
    @Mapping(target = "decSettle", source = "documentedCondition")
    @Mapping(target = "clientCreate", source = "customerCreationReason")
    @Mapping(target = "totalAsset", source = "totalAssets")
    @Mapping(target = "totalLiability", source = "totalLiabilities")
    @Mapping(target = "totalEquity", source = "totalEquity")
    @Mapping(target = "prodAssets", source = "productiveAssets")
    @Mapping(target = "executiveState", source = "politicallyExposed")
    Tx067116TechnicalRequest toTx067116Request(CustomerAdditionalDataUpdateRequestDto request);

    CustomerAdditionalDataUpdateResponseDto toAdditionalResponse(Tx067116TechnicalResponse response);

    @Mapping(target = "cif", source = "customerIdentifier")
    @Mapping(target = "operacion", source = "operationOption")
    @Mapping(target = "clieObligCont", source = "accountingRequired")
    Tx067153TechnicalRequest toTx067153Request(CustomerIdentificationUpdateRequestDto request);

    CustomerIdentificationUpdateResponseDto toIdentificationResponse(Tx067153TechnicalResponse response);

    @Mapping(target = "cifNo", source = "customerIdentifier")
    @Mapping(target = "opcion", source = "operationOption")
    @Mapping(target = "otrNacion", source = "otherNationality")
    @Mapping(target = "tinGin", source = "tinCode")
    @Mapping(target = "repLeg", source = "usLegalRepresentative")
    @Mapping(target = "tribuExt", source = "externalTaxObligations")
    @Mapping(target = "tribuPais", source = "externalTaxCountry")
    @Mapping(target = "nacion2", source = "nationalityTwo")
    @Mapping(target = "nacion3", source = "nationalityThree")
    @Mapping(target = "dirCorrInst", source = "correspondenceAddress")
    Tx062120TechnicalRequest toTx062120Request(CustomerFatcaDataUpdateRequestDto request);

    CustomerFatcaDataUpdateResponseDto toFatcaResponse(Tx062120TechnicalResponse response);
}
