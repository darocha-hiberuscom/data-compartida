package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerIdentificationUpdateResponseDto;
import reactor.core.publisher.Mono;

public interface CustomerIdentificationUpdateOutput {

    Mono<CustomerIdentificationUpdateResponseDto> queryIdentification(CustomerIdentificationUpdateRequestDto request);

    Mono<CustomerIdentificationUpdateResponseDto> updateIdentification(CustomerIdentificationUpdateRequestDto request);
}
