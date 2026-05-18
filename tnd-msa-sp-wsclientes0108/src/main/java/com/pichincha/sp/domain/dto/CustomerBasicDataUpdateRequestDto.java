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
public class CustomerBasicDataUpdateRequestDto {

    private String customerIdentifier;
    private String customerStatus;
    private String primaryLastName;
    private String secondaryLastName;
    private String primaryName;
    private String secondaryName;
}
