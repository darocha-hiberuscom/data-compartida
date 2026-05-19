package com.pichincha.sp.infrastructure.input.adapter.rest.dto.internal;

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
public class SoapBodyInDto {
    private String cif;
    private SoapDatosBasicosDto datosBasicos;
    private SoapDatosAdicionalesDto datosAdicionales;
    private SoapFatcaDto fatca;
}

