package com.pichincha.sp.infrastructure.output.adapter.response;

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
public class Tx067000Response implements BancsStandardResponse {

    private String responseCode;
    private String responseMessage;
}
