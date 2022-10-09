package com.droptheclothes.api.model.dto;

import lombok.Getter;

@Getter
public class AuthInfoResponse {

  //고유id, 어떤 간편로그인인지, 기기 정보
  private String authId;
  private String authType;
  private String deviceID;

  public AuthInfoResponse(String authId, String authType, String deviceID) {
    this.authId = authId;
    this.authType = authType;
    this.deviceID = deviceID;
  }

}
