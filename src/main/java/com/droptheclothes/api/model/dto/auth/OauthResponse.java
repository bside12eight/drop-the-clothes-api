package com.droptheclothes.api.model.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthResponse {

  private String nickName;
  private String email;
  private String type;

  @Builder
  public OauthResponse(
        String nickName
      , String email
      , String type
  ){
    this.nickName = nickName;
    this.email = email;
    this.type = type;
  }

}
