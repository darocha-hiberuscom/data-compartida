package com.pichincha.sp.infrastructure.output.adapter.request;

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
public class Tx067153Request {

    private String cif;
    private String operacion;
    private String clieObligCont;
}
