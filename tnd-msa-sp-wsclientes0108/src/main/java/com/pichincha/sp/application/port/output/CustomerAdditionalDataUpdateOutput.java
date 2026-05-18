package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerAdditionalDataUpdateResponseDto;
import reactor.core.publisher.Mono;

public interface CustomerAdditionalDataUpdateOutput {

    Mono<CustomerAdditionalDataUpdateResponseDto> updateAdditionalData(CustomerAdditionalDataUpdateRequestDto request);
}
