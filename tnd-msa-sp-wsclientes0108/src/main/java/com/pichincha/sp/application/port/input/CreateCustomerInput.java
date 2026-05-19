package com.pichincha.sp.application.port.input;

import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface CreateCustomerInput {

    Mono<CreateCustomerResultDto> execute(@Valid CreateCustomerInputDto request);
}
