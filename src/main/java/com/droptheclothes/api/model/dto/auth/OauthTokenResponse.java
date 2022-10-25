package com.droptheclothes.api.model.dto.auth;

import lombok.Getter;

@Getter
public class OauthTokenResponse {
  private String tokenType; // bearer로 고정
  private String accessToken;
}
