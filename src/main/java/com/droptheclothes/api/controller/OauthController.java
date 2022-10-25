package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OauthController {

  private final OauthService oauthService;

  @PostMapping("/api/oauth/login")
  public ApiResponse saveAccessToken(@RequestBody OauthInfoRequest request) {

    oauthService.saveAccessToken(request);

    return new ApiResponse(
        ApiResponseHeader.create(ResultCode.SUCCESS)
        , new SingleObject<>(new OauthInfoRequest(request.getAuthId(), request.getAuthType(),
        request.getDeviceID()))
    );
  }

  /**
   * OAuth 로그인 시 인증코드를 넘겨받은 후
   *  1) 최초 로그인 : 회원가입 처리 -> 메인
   *  2) 기타 : 로그인 처리
   * @param provider
   * @param code
   * @return
   */
  @PostMapping("/api/login/oauth/{provider}")
  public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
    LoginResponse loginResponse = oauthService.login(provider, code);
    return ResponseEntity.ok().body(loginResponse);
  }


  /**
   * OAuth 로그인 시 액세스코드를 넘겨받은 후
   *  1) 최초 로그인 : 회원가입 처리 -> 메인
   *  2) 기타 : 로그인 처리
   * @param provider
   * @param accessToken
   * @return
   */
  @PostMapping("/api/login/oauth2/{provider}")
  public ResponseEntity<LoginResponse> loginWithToken(@PathVariable String provider, @RequestParam String accessToken) {
    LoginResponse loginResponse = oauthService.loginWithToken(provider, accessToken);
    return ResponseEntity.ok().body(loginResponse);
  }


}
