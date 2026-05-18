package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateRequestDto;
import com.pichincha.sp.domain.dto.CustomerBasicDataUpdateResponseDto;
import reactor.core.publisher.Mono;

public interface CustomerBasicDataUpdateOutput {

    Mono<CustomerBasicDataUpdateResponseDto> updateBasicData(CustomerBasicDataUpdateRequestDto request);
}
