package com.droptheclothes.api.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String memberId;
    private String nickName;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String type;
}
