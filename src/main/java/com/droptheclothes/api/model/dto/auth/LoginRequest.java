package com.droptheclothes.api.model.dto.auth;

import lombok.Getter;

@Getter
public class LoginRequest {

  private String nickName;
  private String accessToken;
  private String type;


}
