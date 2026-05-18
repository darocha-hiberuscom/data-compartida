package com.pichincha.sp.application.service;

import com.pichincha.sp.application.port.input.CreateCustomerInput;
import com.pichincha.sp.application.port.output.CustomerAdditionalDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerBasicDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerFatcaDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerIdentificationUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerLookupOutput;
import com.pichincha.sp.application.port.output.CustomerPersonalDataUpdateOutput;
import com.pichincha.sp.domain.constants.CustomerFlowConstants;
import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import com.pichincha.sp.domain.exception.BusinessException;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateCustomerService implements CreateCustomerInput {

    private final CustomerLookupOutput customerLookupOutput;
    private final CustomerBasicDataUpdateOutput customerBasicDataUpdateOutput;
    private final CustomerPersonalDataUpdateOutput customerPersonalDataUpdateOutput;
    private final CustomerAdditionalDataUpdateOutput customerAdditionalDataUpdateOutput;
    private final CustomerIdentificationUpdateOutput customerIdentificationUpdateOutput;
    private final CustomerFatcaDataUpdateOutput customerFatcaDataUpdateOutput;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CreateCustomerResultDto> execute(CreateCustomerInputDto request) {
        return validateRequest(request)
                .then(runCreationFlow(request))
                .doFinally(signalType -> customLogLevelHandler.log(
                        CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        "Create customer flow finished with signal: {}",
                        signalType));
    }

    private Mono<Void> validateRequest(CreateCustomerInputDto request) {
        if (request == null || request.getCustomerIdentifier() == null || request.getCustomerIdentifier().isBlank()) {
            return Mono.error(new BusinessException(CustomerFlowConstants.EMPTY_IDENTIFIER_MESSAGE));
        }
        return Mono.empty();
    }

    private Mono<CreateCustomerResultDto> runCreationFlow(CreateCustomerInputDto request) {
        CustomerLookupRequestDto lookupOptionOne = CustomerLookupRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_ONE)
                .build();

        CustomerLookupRequestDto lookupOptionFive = CustomerLookupRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_FIVE)
                .build();

        CustomerLookupRequestDto lookupOptionTwentyOne = CustomerLookupRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_TWENTY_ONE)
                .build();

        CustomerFatcaDataUpdateRequestDto fatcaOptionOne = buildFatcaRequest(request, CustomerFlowConstants.OPTION_ONE);
        CustomerFatcaDataUpdateRequestDto fatcaOptionThree = buildFatcaRequest(request, CustomerFlowConstants.OPTION_THREE);

        return customerLookupOutput.findCustomer(lookupOptionOne)
                .flatMap(lookup -> customerBasicDataUpdateOutput.updateBasicData(buildBasicDataRequest(request)))
                .flatMap(result -> customerLookupOutput.findCustomer(lookupOptionFive))
                .flatMap(lookup -> customerPersonalDataUpdateOutput.updatePersonalData(buildPersonalDataRequest(request)))
                .flatMap(result -> customerLookupOutput.findCustomer(lookupOptionTwentyOne))
                .flatMap(lookup -> customerAdditionalDataUpdateOutput.updateAdditionalData(buildAdditionalDataRequest(request)))
                .flatMap(result -> customerIdentificationUpdateOutput.queryIdentification(buildIdentificationQueryRequest(request)))
                .flatMap(result -> customerIdentificationUpdateOutput.updateIdentification(buildIdentificationUpdateRequest(request)))
                .flatMap(result -> customerFatcaDataUpdateOutput.updateFatcaData(fatcaOptionOne)
                        .flatMap(fatcaResponse -> retryFatcaIfNeeded(fatcaResponse, fatcaOptionThree)))
                .map(response -> CreateCustomerResultDto.builder()
                        .code(CustomerFlowConstants.SUCCESS_CODE)
                        .message(CustomerFlowConstants.SUCCESS_MESSAGE)
                        .type(CustomerFlowConstants.ERROR_TYPE)
                        .build())
                .onErrorMap(error -> {
                    customLogLevelHandler.log(
                            CustomLogLevel.ERROR,
                            Thread.currentThread().getStackTrace(),
                            "Create customer flow error: {}",
                            error.getMessage() == null ? "unknown" : error.getMessage());
                    if (error instanceof BusinessException) {
                        return error;
                    }
                    return new BusinessException("Create customer flow failed", error);
                });
    }

    private Mono<CustomerFatcaDataUpdateResponseDto> retryFatcaIfNeeded(
            CustomerFatcaDataUpdateResponseDto response,
            CustomerFatcaDataUpdateRequestDto retryRequest) {
        if (response != null && CustomerFlowConstants.FATCA_RETRY_CODE.equals(response.getResponseCode())) {
            return customerFatcaDataUpdateOutput.updateFatcaData(retryRequest);
        }
        return Mono.justOrEmpty(response);
    }

    private CustomerBasicDataUpdateRequestDto buildBasicDataRequest(CreateCustomerInputDto request) {
        return CustomerBasicDataUpdateRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .customerStatus(request.getCustomerStatus())
                .primaryLastName(request.getPrimaryLastName())
                .secondaryLastName(request.getSecondaryLastName())
                .primaryName(request.getPrimaryName())
                .secondaryName(request.getSecondaryName())
                .build();
    }

    private CustomerPersonalDataUpdateRequestDto buildPersonalDataRequest(CreateCustomerInputDto request) {
        return CustomerPersonalDataUpdateRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_FIVE)
                .birthDate(request.getBirthDate())
                .birthPlace(request.getBirthPlace())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .workPlace(request.getWorkPlace())
                .build();
    }

    private CustomerAdditionalDataUpdateRequestDto buildAdditionalDataRequest(CreateCustomerInputDto request) {
        return CustomerAdditionalDataUpdateRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_TWENTY_ONE)
                .spouseName(request.getSpouseName())
                .age(request.getAge())
                .documentedCondition(request.getDocumentedCondition())
                .customerCreationReason(request.getCustomerCreationReason())
                .build();
    }

    private CustomerIdentificationUpdateRequestDto buildIdentificationQueryRequest(CreateCustomerInputDto request) {
        return CustomerIdentificationUpdateRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_TWO)
                .accountingRequired(request.getAccountingRequired())
                .build();
    }

    private CustomerIdentificationUpdateRequestDto buildIdentificationUpdateRequest(CreateCustomerInputDto request) {
        return CustomerIdentificationUpdateRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(CustomerFlowConstants.OPTION_ONE)
                .accountingRequired(request.getAccountingRequired())
                .build();
    }

    private CustomerFatcaDataUpdateRequestDto buildFatcaRequest(CreateCustomerInputDto request, String option) {
        return CustomerFatcaDataUpdateRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .operationOption(option)
                .otherNationality(request.getOtherNationality())
                .tinCode(request.getTinCode())
                .usLegalRepresentative(request.getUsLegalRepresentative())
                .externalTaxObligations(request.getExternalTaxObligations())
                .externalTaxCountry(request.getExternalTaxCountry())
                .nationalityTwo(request.getNationalityTwo())
                .nationalityThree(request.getNationalityThree())
                .correspondenceAddress(request.getCorrespondenceAddress())
                .build();
    }
}