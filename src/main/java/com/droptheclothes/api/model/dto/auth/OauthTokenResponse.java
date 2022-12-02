package com.droptheclothes.api.model.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthTokenResponse {

    private String tokenType; // bearer로 고정
    private String accessToken;

    @Builder
    public OauthTokenResponse(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }
}
