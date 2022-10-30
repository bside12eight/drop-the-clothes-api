package com.droptheclothes.api.model.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

  private String nickName;
  private String email;
  private String accessToken;
  private String refreshToken;

  @Builder
  public LoginResponse(
        String nickName
      , String email
      , String accessToken
      , String refreshToken
  ){
    this.nickName = nickName;
    this.email = email;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

}
