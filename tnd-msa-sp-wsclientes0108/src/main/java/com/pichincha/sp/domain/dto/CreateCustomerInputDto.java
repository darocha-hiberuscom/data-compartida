package com.pichincha.sp.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class CreateCustomerInputDto {

    @NotBlank(message = "Valor del campo CIF vacio o invalido")
    @Pattern(regexp = "^(?!0$).+", message = "Valor del campo CIF vacio o invalido")
    private String customerIdentifier;
    private String primaryLastName;
    private String secondaryLastName;
    private String primaryName;
    private String secondaryName;
    private String customerStatus;
    private String birthDate;
    private String birthPlace;
    private String gender;
    private String maritalStatus;
    private String workPlace;
    private String spouseName;
    private String age;
    private String documentedCondition;
    private String customerCreationReason;
    private String accountingRequired;
    private String otherNationality;
    private String tinCode;
    private String usLegalRepresentative;
    private String externalTaxObligations;
    private String externalTaxCountry;
    private String nationalityTwo;
    private String nationalityThree;
    private String correspondenceAddress;
}
