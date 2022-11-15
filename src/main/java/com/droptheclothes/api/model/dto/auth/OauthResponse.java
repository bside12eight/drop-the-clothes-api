package com.droptheclothes.api.model.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthResponse {

  private String accessToken;
  private String nickName;
  private String email;
  private String type;

  @Builder
  public OauthResponse(
        String accessToken
      , String nickName
      , String email
      , String type
  ){
    this.accessToken = accessToken;
    this.nickName = nickName;
    this.email = email;
    this.type = type;
  }

}
