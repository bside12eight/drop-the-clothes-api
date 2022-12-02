package com.droptheclothes.api.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OauthResponse {

    private String identityToken;

    private String accessToken;

    private String nickName;

    private String email;

    private String type;
}
