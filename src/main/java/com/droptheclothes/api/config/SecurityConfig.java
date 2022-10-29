package com.droptheclothes.api.config;

import com.droptheclothes.api.model.enums.Role;
import com.droptheclothes.api.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final OauthService oauthService;

  public SecurityConfig(OauthService oauthService) {
    this.oauthService = oauthService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.oauth2Login() // OAuth2 로그인 설정 시작점
        .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
        .userService(oauthService); // OAuth2 로그인 성공 시, 후작업을 진행할 UserService 인터페이스 구현체 등록
  }
}