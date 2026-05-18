package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.CustomerLookupRequestDto;
import com.pichincha.sp.domain.dto.CustomerLookupResponseDto;
import reactor.core.publisher.Mono;

public interface CustomerLookupOutput {

    Mono<CustomerLookupResponseDto> findCustomer(CustomerLookupRequestDto request);
}
