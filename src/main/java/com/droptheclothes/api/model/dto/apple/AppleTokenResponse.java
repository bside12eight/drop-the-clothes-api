package com.droptheclothes.api.model.dto.apple;

import lombok.Getter;

@Getter
public class AppleTokenResponse {

    private String accessToken;

    private int expiresIn;

    private String idToken;

    private String refreshToken;

    private String tokenType;

    private String error;

    private String errorDescription;
}
