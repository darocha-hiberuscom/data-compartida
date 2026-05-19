package com.pichincha.sp.infrastructure.output.adapter.mapper;

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
import com.pichincha.sp.infrastructure.output.adapter.request.Tx062120Request;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067000Request;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067050Request;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067101Request;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067116Request;
import com.pichincha.sp.infrastructure.output.adapter.request.Tx067153Request;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx062120Response;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067000Response;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067050Response;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067101Response;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067116Response;
import com.pichincha.sp.infrastructure.output.adapter.response.Tx067153Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BancsDomainMapper {

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    Tx067050Request toTx067050Request(CustomerLookupRequestDto request);

    @Mapping(target = "customerIdentifier", source = "accntNumber1")
    @Mapping(target = "payloadReference", source = "accntNumber1")
    CustomerLookupResponseDto toLookupResponse(Tx067050Response response);

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "defInteger4", source = "customerStatus")
    @Mapping(target = "defaultString2", source = "primaryLastName")
    @Mapping(target = "name1", source = "primaryName")
    @Mapping(target = "defaultString3", source = "secondaryLastName")
    @Mapping(target = "name2", source = "secondaryName")
    Tx067000Request toTx067000Request(CustomerBasicDataUpdateRequestDto request);

    CustomerBasicDataUpdateResponseDto toBasicResponse(Tx067000Response response);

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "date1", source = "birthDate")
    @Mapping(target = "defaultString8", source = "birthPlace")
    @Mapping(target = "defaultString2", source = "gender")
    @Mapping(target = "defaultString3", source = "maritalStatus")
    @Mapping(target = "defaultString5", source = "workPlace")
    Tx067101Request toTx067101Request(CustomerPersonalDataUpdateRequestDto request);

    CustomerPersonalDataUpdateResponseDto toPersonalResponse(Tx067101Response response);

    @Mapping(target = "accntNumber1", source = "customerIdentifier")
    @Mapping(target = "custAge", source = "age")
    @Mapping(target = "decSettle", source = "documentedCondition")
    @Mapping(target = "clientCreate", source = "customerCreationReason")
    @Mapping(target = "totalAsset", source = "totalAssets")
    @Mapping(target = "totalLiability", source = "totalLiabilities")
    @Mapping(target = "totalEquity", source = "totalEquity")
    @Mapping(target = "prodAssets", source = "productiveAssets")
    @Mapping(target = "executiveState", source = "politicallyExposed")
    Tx067116Request toTx067116Request(CustomerAdditionalDataUpdateRequestDto request);

    CustomerAdditionalDataUpdateResponseDto toAdditionalResponse(Tx067116Response response);

    @Mapping(target = "cif", source = "customerIdentifier")
    @Mapping(target = "operacion", source = "operationOption")
    @Mapping(target = "clieObligCont", source = "accountingRequired")
    Tx067153Request toTx067153Request(CustomerIdentificationUpdateRequestDto request);

    CustomerIdentificationUpdateResponseDto toIdentificationResponse(Tx067153Response response);

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
    Tx062120Request toTx062120Request(CustomerFatcaDataUpdateRequestDto request);

    CustomerFatcaDataUpdateResponseDto toFatcaResponse(Tx062120Response response);
}
