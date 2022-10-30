package com.droptheclothes.api.model.dto.auth;

import com.droptheclothes.api.model.enums.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

  private String memberId;
  private String provider; // joinType
  private String nickName;
  private String email; // UQ
  private String password;
  private LocalDateTime loggedInAt;
  private LocalDateTime deletedAt;
  private Role role;
  private String tokenType;
  private String accessToken;
  private String refreshToken;

  @Builder
  public LoginResponse(
      String memberId
      , String provider
      , String nickName
      , String email
      , String password
      , LocalDateTime loggedInAt
      , LocalDateTime deletedAt
      , Role role
      , String tokenType
      , String accessToken
      , String refreshToken

  ){
    this.memberId = memberId;
    this.provider = provider;
    this.nickName = nickName;
    this.email = email;
    this.password = password;
    this.loggedInAt = loggedInAt;
    this.deletedAt = deletedAt;
    this.role = role;
    this.tokenType = tokenType;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

}
