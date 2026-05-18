package com.pichincha.sp.application.port.input;

import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import reactor.core.publisher.Mono;

public interface CreateCustomerInput {

    Mono<CreateCustomerResultDto> execute(CreateCustomerInputDto request);
}
