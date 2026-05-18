package com.pichincha.sp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLookupResponseDto {

    private String customerIdentifier;
    private String payloadReference;
    private String responseCode;
    private String responseMessage;
}
