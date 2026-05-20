package com.pichincha.sp.application.service;

import com.pichincha.sp.application.mappers.CreateCustomerRequestMapper;
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
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.exception.BusinessException;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
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
    private final CreateCustomerRequestMapper requestMapper;

    @Override
    @BpLogger
    public Mono<CreateCustomerResultDto> execute(@Valid CreateCustomerInputDto request) {
        CustomerLookupRequestDto lookupRequest = requestMapper.buildLookupRequest(request);
        CustomerFatcaDataUpdateRequestDto fatcaOptionOne = requestMapper.buildFatcaRequest(request, CustomerFlowConstants.OPTION_ONE);
        CustomerFatcaDataUpdateRequestDto fatcaOptionThree = requestMapper.buildFatcaRequest(request, CustomerFlowConstants.OPTION_THREE);

        return customerDetailCreateOutput.createCustomerDetail(lookupRequest)
                .flatMap(lookup -> customerBasicDataUpdateOutput.updateBasicData(requestMapper.buildBasicDataRequest(request)))
                .flatMap(result -> customerPersonalDetailOutput.getCustomerPersonalDetail(lookupRequest))
                .flatMap(lookup -> customerPersonalDataUpdateOutput.updatePersonalData(requestMapper.buildPersonalDataRequest(request)))
                .flatMap(result -> customerAdditionalDetailOutput.getCustomerAdditionalDetail(lookupRequest))
                .flatMap(lookup -> customerAdditionalDataUpdateOutput.updateAdditionalData(requestMapper.buildAdditionalDataRequest(request)))
                .flatMap(result -> customerIdentificationUpdateOutput.queryIdentification(requestMapper.buildIdentificationQueryRequest(request)))
                .flatMap(result -> customerIdentificationUpdateOutput.updateIdentification(requestMapper.buildIdentificationUpdateRequest(request)))
                .flatMap(result -> handlePoliticallyExposedFlow(request, lookupRequest, fatcaOptionOne, fatcaOptionThree, result.getPoliticallyExposed()))
                .map(response -> CreateCustomerResultDto.builder()
                        .code(CustomerFlowConstants.SUCCESS_CODE)
                        .message(CustomerFlowConstants.SUCCESS_MESSAGE)
                        .type(CustomerFlowConstants.SUCCESS_TYPE)
                        .build())
                .onErrorMap(error -> error instanceof BusinessException
                        ? error
                        : new BusinessException("Create customer flow failed", error));
    }

    private Mono<CustomerFatcaDataUpdateResponseDto> handlePoliticallyExposedFlow(
            CreateCustomerInputDto request,
            CustomerLookupRequestDto lookupRequest,
            CustomerFatcaDataUpdateRequestDto fatcaOptionOne,
            CustomerFatcaDataUpdateRequestDto fatcaOptionThree,
            String politicallyExposed) {

        if (politicallyExposed != null && !politicallyExposed.isBlank()) {
            return customerAdditionalDetailOutput.getCustomerAdditionalDetail(lookupRequest)
                    .flatMap(lookup -> customerAdditionalDataUpdateOutput.updateAdditionalData(requestMapper.buildAdditionalDataRequest(request)))
                    .flatMap(r -> customerFatcaDataUpdateOutput.updateFatcaData(fatcaOptionOne)
                            .flatMap(fatcaResponse -> retryFatcaIfNeeded(fatcaResponse, fatcaOptionThree)));
        }

        return customerFatcaDataUpdateOutput.updateFatcaData(fatcaOptionOne)
                .flatMap(fatcaResponse -> retryFatcaIfNeeded(fatcaResponse, fatcaOptionThree));
    }

    private Mono<CustomerFatcaDataUpdateResponseDto> retryFatcaIfNeeded(
            CustomerFatcaDataUpdateResponseDto response,
            CustomerFatcaDataUpdateRequestDto retryRequest) {

        if (response != null && CustomerFlowConstants.FATCA_RETRY_CODE.equals(response.getResponseCode())) {
            return customerFatcaDataUpdateOutput.updateFatcaData(retryRequest);
        }
        return Mono.justOrEmpty(response);
    }
}
