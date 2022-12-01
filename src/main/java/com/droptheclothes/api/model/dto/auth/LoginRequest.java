package com.droptheclothes.api.model.dto.auth;

import com.droptheclothes.api.exception.AuthenticationException;
import com.droptheclothes.api.model.dto.apple.IdentityTokenHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import lombok.Getter;

@Getter
public class LoginRequest {

    private String accessToken;

    private String identityToken;

    public IdentityTokenHeader getIdentityTokenHeader() {
        String headerString = identityToken.substring(0, identityToken.indexOf("."));

        try {
            return new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerString), "UTF-8"), IdentityTokenHeader.class);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new AuthenticationException("올바르지 않은 토큰입니다.");
        }
    }
}
