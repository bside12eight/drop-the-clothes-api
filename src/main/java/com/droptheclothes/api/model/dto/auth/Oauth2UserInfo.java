package com.droptheclothes.api.model.dto.auth;

public interface Oauth2UserInfo {
  public abstract String getProvider();
  public abstract String getProviderId();
  public abstract String getNickName();
  public abstract String getEmail();
  //public abstract String getName();

}