package com.pichincha.sp.application.mappers;

import com.pichincha.sp.domain.constants.CustomerFlowConstants;
import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateCustomerRequestMapper {

    CustomerLookupRequestDto buildLookupRequest(CreateCustomerInputDto request);

    CustomerBasicDataUpdateRequestDto buildBasicDataRequest(CreateCustomerInputDto request);

    @Mapping(target = "operationOption", expression = "java(CustomerFlowConstants.OPTION_FIVE)")
    CustomerPersonalDataUpdateRequestDto buildPersonalDataRequest(CreateCustomerInputDto request);

    @Mapping(target = "operationOption", expression = "java(CustomerFlowConstants.OPTION_TWENTY_ONE)")
    CustomerAdditionalDataUpdateRequestDto buildAdditionalDataRequest(CreateCustomerInputDto request);

    @Mapping(target = "operationOption", expression = "java(CustomerFlowConstants.OPTION_TWO)")
    CustomerIdentificationUpdateRequestDto buildIdentificationQueryRequest(CreateCustomerInputDto request);

    @Mapping(target = "operationOption", expression = "java(CustomerFlowConstants.OPTION_ONE)")
    CustomerIdentificationUpdateRequestDto buildIdentificationUpdateRequest(CreateCustomerInputDto request);

    @Mapping(target = "customerIdentifier", source = "request.customerIdentifier")
    @Mapping(target = "operationOption", source = "option")
    @Mapping(target = "otherNationality", source = "request.otherNationality")
    @Mapping(target = "tinCode", source = "request.tinCode")
    @Mapping(target = "usLegalRepresentative", source = "request.usLegalRepresentative")
    @Mapping(target = "externalTaxObligations", source = "request.externalTaxObligations")
    @Mapping(target = "externalTaxCountry", source = "request.externalTaxCountry")
    @Mapping(target = "nationalityTwo", source = "request.nationalityTwo")
    @Mapping(target = "nationalityThree", source = "request.nationalityThree")
    @Mapping(target = "correspondenceAddress", source = "request.correspondenceAddress")
    CustomerFatcaDataUpdateRequestDto buildFatcaRequest(CreateCustomerInputDto request, String option);
}

