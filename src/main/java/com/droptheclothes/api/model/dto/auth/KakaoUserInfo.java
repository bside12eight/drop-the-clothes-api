package com.droptheclothes.api.model.dto.auth;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo {

  private Map<String, Object> attributes;

  public KakaoUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }
  @Override
  public String getProviderId() {
    return String.valueOf(attributes.get("id"));
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getEmail() {
    return (String) getKakaoAccount().get("email");
  }

  @Override
  public String getNickName() {
    return (String) getProfile().get("nickname");
  }

  public Map<String, Object> getKakaoAccount(){
    return(Map<String, Object>) attributes.get("kakao_account");
  }

  public Map<String, Object> getProfile(){
    return (Map<String, Object>) getKakaoAccount().get("profile");
  }
}