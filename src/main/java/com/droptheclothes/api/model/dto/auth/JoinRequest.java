package com.droptheclothes.api.model.dto.auth;

import lombok.Getter;

@Getter
public class JoinRequest {

  private String nickName;
  private String accessToken;
}
