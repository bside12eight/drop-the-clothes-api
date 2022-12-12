package com.droptheclothes.api.model.dto.apple;

import lombok.Getter;

@Getter
public class AppleTokenResponse {

    private String access_token;

    private int expires_in;

    private String id_token;

    private String refresh_token;

    private String token_type;
}
