package com.pichincha.sp.application.service;

import com.pichincha.sp.application.port.input.CreateCustomerInput;
import com.pichincha.sp.application.port.output.CustomerAdditionalDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerAdditionalDetailOutput;
import com.pichincha.sp.application.port.output.CustomerBasicDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerDetailCreateOutput;
import com.pichincha.sp.application.port.output.CustomerFatcaDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerIdentificationUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerPersonalDataUpdateOutput;
import com.pichincha.sp.application.port.output.CustomerPersonalDetailOutput;
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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Set;

@Service
@Validated
@RequiredArgsConstructor
public class CreateCustomerService implements CreateCustomerInput {

    private final CustomerDetailCreateOutput customerDetailCreateOutput;
    private final CustomerPersonalDetailOutput customerPersonalDetailOutput;
    private final CustomerAdditionalDetailOutput customerAdditionalDetailOutput;
    private final CustomerBasicDataUpdateOutput customerBasicDataUpdateOutput;
    private final CustomerPersonalDataUpdateOutput customerPersonalDataUpdateOutput;
    private final CustomerAdditionalDataUpdateOutput customerAdditionalDataUpdateOutput;
    private final CustomerIdentificationUpdateOutput customerIdentificationUpdateOutput;
    private final CustomerFatcaDataUpdateOutput customerFatcaDataUpdateOutput;
    private final Validator validator;
    private final CustomLogLevelHandler customLogLevelHandler;

    @Override
    @BpLogger
    public Mono<CreateCustomerResultDto> execute(@Valid CreateCustomerInputDto request) {
        Set<ConstraintViolation<CreateCustomerInputDto>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            return Mono.just(buildValidationErrorResult(violations))
                    .doFinally(signalType -> logFlowFinished(signalType.toString()));
        }

        return runCreationFlow(request)
                .doFinally(signalType -> logFlowFinished(signalType.toString()));
    }

    private CreateCustomerResultDto buildValidationErrorResult(Set<ConstraintViolation<CreateCustomerInputDto>> violations) {
        String message = violations.stream()
                .min(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .map(ConstraintViolation::getMessage)
                .orElse(CustomerFlowConstants.EMPTY_IDENTIFIER_MESSAGE);

        return CreateCustomerResultDto.builder()
                .code(CustomerFlowConstants.EMPTY_IDENTIFIER_CODE)
                .message(message)
                .type(CustomerFlowConstants.ERROR_TYPE)
                .build();
    }

    private void logFlowFinished(String signalType) {
        customLogLevelHandler.log(
                CustomLogLevel.INFO,
                Thread.currentThread().getStackTrace(),
                "Create customer flow finished with signal: {}",
                signalType);
    }

    private Mono<CreateCustomerResultDto> runCreationFlow(CreateCustomerInputDto request) {
        CustomerLookupRequestDto lookupRequest = CustomerLookupRequestDto.builder()
                .customerIdentifier(request.getCustomerIdentifier())
                .build();

        CustomerFatcaDataUpdateRequestDto fatcaOptionOne = buildFatcaRequest(request, CustomerFlowConstants.OPTION_ONE);
        CustomerFatcaDataUpdateRequestDto fatcaOptionThree = buildFatcaRequest(request, CustomerFlowConstants.OPTION_THREE);

        return customerDetailCreateOutput.createCustomerDetail(lookupRequest)
                .flatMap(lookup -> customerBasicDataUpdateOutput.updateBasicData(buildBasicDataRequest(request)))
                .flatMap(result -> customerPersonalDetailOutput.getCustomerPersonalDetail(lookupRequest))
                .flatMap(lookup -> customerPersonalDataUpdateOutput.updatePersonalData(buildPersonalDataRequest(request)))
                .flatMap(result -> customerAdditionalDetailOutput.getCustomerAdditionalDetail(lookupRequest))
                .flatMap(lookup -> customerAdditionalDataUpdateOutput.updateAdditionalData(buildAdditionalDataRequest(request)))
                .flatMap(result -> customerIdentificationUpdateOutput.queryIdentification(buildIdentificationQueryRequest(request)))
                .flatMap(result -> customerIdentificationUpdateOutput.updateIdentification(buildIdentificationUpdateRequest(request)))
                .flatMap(result -> {
                    if (result.getPoliticallyExposed() != null && !result.getPoliticallyExposed().isBlank()) {
                        return customerAdditionalDetailOutput.getCustomerAdditionalDetail(lookupRequest)
                                .flatMap(lookup -> customerAdditionalDataUpdateOutput.updateAdditionalData(buildAdditionalDataRequest(request)))
                                .flatMap(r -> customerFatcaDataUpdateOutput.updateFatcaData(fatcaOptionOne)
                                        .flatMap(fatcaResponse -> retryFatcaIfNeeded(fatcaResponse, fatcaOptionThree)));
                    }
                    return customerFatcaDataUpdateOutput.updateFatcaData(fatcaOptionOne)
                            .flatMap(fatcaResponse -> retryFatcaIfNeeded(fatcaResponse, fatcaOptionThree));
                })
                .map(response -> CreateCustomerResultDto.builder()
                        .code(CustomerFlowConstants.SUCCESS_CODE)
                        .message(CustomerFlowConstants.SUCCESS_MESSAGE)
                        .type(CustomerFlowConstants.SUCCESS_TYPE)
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