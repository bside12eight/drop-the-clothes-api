package com.droptheclothes.api.model.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

  private String memberId;
  private String nickName;
  private String email;
  private String accessToken;
  private String refreshToken;
  private String type;

  @Builder
  public LoginResponse(
        String memberId
      , String nickName
      , String email
      , String accessToken
      , String refreshToken
      , String type
  ){
    this.memberId = memberId;
    this.nickName = nickName;
    this.email = email;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.type = type;
  }

}
