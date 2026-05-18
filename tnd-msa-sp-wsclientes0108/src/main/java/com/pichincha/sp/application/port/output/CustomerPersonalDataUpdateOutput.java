package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerPersonalDataUpdateResponseDto;
import reactor.core.publisher.Mono;

public interface CustomerPersonalDataUpdateOutput {

    Mono<CustomerPersonalDataUpdateResponseDto> updatePersonalData(CustomerPersonalDataUpdateRequestDto request);
}
