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
public class BusinessErrorDto {
    private String code;
    private String message;
    private String businessMessage;
    private String type;
    private String resource;
    private String component;
    private String backend;
}

