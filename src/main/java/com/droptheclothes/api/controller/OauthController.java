package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
   * OAuth 로그인 시 소셜 서버로부터 액세스코드를 넘겨받은 후
   *  1) 최초 로그인 : 회원가입 처리 -> 메인
   *  2) 기타 : 로그인 처리
   */
  @GetMapping("/api/oauth2/{provider}")
  public ApiResponse loginWithToken(@PathVariable String provider, @RequestParam String accessToken) {
    LoginResponse loginResponse = oauthService.loginWithToken(provider, accessToken);
    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(loginResponse));
  }

  @GetMapping("/api/oauth2/{provider}")
  public ApiResponse loginWithToken2(@PathVariable String provider, @RequestParam String accessToken,@RequestParam String type) {

    LoginResponse loginResponse = null;

    // 최초 로그인 시
    if(type == null){
      loginResponse = oauthService.loginWithToken2(provider, accessToken, type);
    }
    // 회원가입시
    else if(type.equals("join")){
      loginResponse = oauthService.loginWithToken(provider, accessToken);
    }
    
    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(loginResponse));
  }

  @PostMapping("/api/oauth2/refresh")
  public ResponseEntity<TokenResponse> refreshToken(@RequestParam String nickName, @RequestParam String refreshToken) {
    TokenResponse tokenResponse = oauthService.refreshToken(nickName, refreshToken);
    return ResponseEntity.ok().body(tokenResponse);
  }

  @GetMapping("/api/oauth2/{nickname}")
  public ResponseEntity<Boolean> checkNickName(@PathVariable String nickName) {
    Boolean checkNickName = oauthService.checkNickName(nickName);
    return ResponseEntity.ok().body(checkNickName);
  }

  @DeleteMapping("/api/oauth2/{memberId}")
  public ResponseEntity<Boolean> deleteProfile(@RequestParam String memberId) {
    Boolean isDelete = oauthService.deleteProfile(memberId);
    return ResponseEntity.ok().body(isDelete);
  }


}
