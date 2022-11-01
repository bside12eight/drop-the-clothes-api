package com.droptheclothes.api.model.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
  private String accessToken;
  private String refreshToken;

  @Builder
  public TokenResponse(
        String accessToken
      , String refreshToken
  ){
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
