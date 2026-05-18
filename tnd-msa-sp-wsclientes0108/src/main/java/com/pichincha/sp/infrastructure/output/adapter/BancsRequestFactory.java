package com.pichincha.sp.infrastructure.output.adapter;

import com.pichincha.bnc.apiclient.dto.request.BancsHeaders;
import com.pichincha.bnc.apiclient.dto.request.BancsRequest;
import com.pichincha.bnc.apiclient.dto.request.user.BancsUser;
import com.pichincha.bnc.apiclient.dto.request.user.HomologateUser;
import org.springframework.stereotype.Component;

@Component
public class BancsRequestFactory {

    private static final String DEFAULT_TELLER = "00000000";
    private static final String DEFAULT_TERMINAL = "000000";
    private static final String DEFAULT_INSTITUTION = "000001";
    private static final String DEFAULT_BRANCH = "00001";
    private static final String DEFAULT_WORKSTATION = "001";
    private static final String DEFAULT_CHANNEL = "I";
    private static final String DEFAULT_APPLICATION = "BEP";
    private static final String DEFAULT_HOMOLOGATE_USER = "USINTERT";

    public <T> BancsRequest<T> buildRequest(String transactionId, T body) {
        BancsUser bancsUser = BancsUser.builder()
                .teller(DEFAULT_TELLER)
                .terminal(DEFAULT_TERMINAL)
                .institution(DEFAULT_INSTITUTION)
                .branch(DEFAULT_BRANCH)
                .workstation(DEFAULT_WORKSTATION)
                .channel(DEFAULT_CHANNEL)
                .application(DEFAULT_APPLICATION)
                .build();

        BancsHeaders headers = BancsHeaders.builder()
                .bancsUser(bancsUser)
                .homologateUser(new HomologateUser(DEFAULT_HOMOLOGATE_USER))
                .build();

        return BancsRequest.<T>builder()
                .transactionId(transactionId)
                .header(headers)
                .body(body)
                .build();
    }
}