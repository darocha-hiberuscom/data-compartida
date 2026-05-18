package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerFatcaDataUpdateResponseDto;
import reactor.core.publisher.Mono;

public interface CustomerFatcaDataUpdateOutput {

    Mono<CustomerFatcaDataUpdateResponseDto> updateFatcaData(CustomerFatcaDataUpdateRequestDto request);
}
