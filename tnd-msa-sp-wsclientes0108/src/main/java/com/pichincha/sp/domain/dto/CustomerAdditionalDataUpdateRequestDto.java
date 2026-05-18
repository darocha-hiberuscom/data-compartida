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
public class CustomerAdditionalDataUpdateRequestDto {

    private String customerIdentifier;
    private String operationOption;
    private String spouseName;
    private String age;
    private String documentedCondition;
    private String customerCreationReason;
    private String annualSales;
    private String totalAssets;
    private String totalLiabilities;
    private String totalEquity;
    private String equityDate;
    private String productiveAssets;
    private String financialDataDate;
    private String politicallyExposed;
}
