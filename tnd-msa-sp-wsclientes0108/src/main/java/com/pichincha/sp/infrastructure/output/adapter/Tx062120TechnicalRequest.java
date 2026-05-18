package com.pichincha.sp.infrastructure.output.adapter;

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
public class Tx062120TechnicalRequest {

    private String cifNo;
    private String opcion;
    private String otrNacion;
    private String tinGin;
    private String repLeg;
    private String tribuExt;
    private String tribuPais;
    private String nacion2;
    private String nacion3;
    private String dirCorrInst;
}
