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
public class CustomerFatcaDataUpdateRequestDto {

    private String customerIdentifier;
    private String operationOption;
    private String otherNationality;
    private String tinCode;
    private String usLegalRepresentative;
    private String externalTaxObligations;
    private String externalTaxCountry;
    private String nationalityTwo;
    private String nationalityThree;
    private String correspondenceAddress;
}
