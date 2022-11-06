package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.auth.JoinRequest;
import com.droptheclothes.api.model.dto.auth.LoginRequest;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.OauthResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OauthController {

  private final OauthService oauthService;

  @PostMapping(value = "/api/oauth2/{provider}")
  public ApiResponse loginWithToken2(@PathVariable String provider, @RequestBody LoginRequest loginRequest) {

    String accessToken = loginRequest.getAccessToken();
    OauthResponse oauthResponse = null;

    oauthResponse = oauthService.checkExistMemberWithToken(provider, accessToken);

    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(oauthResponse));
  }

  @PostMapping(value = "/api/oauth2/{provider}/singup")
  public ApiResponse join(@PathVariable String provider, @RequestBody JoinRequest joinRequest) {

    String accessToken = joinRequest.getAccessToken();
    String nickName = "";

    LoginResponse loginResponse = null;

    // 닉네임 변경하지 않고 회원가입시
    if(joinRequest.getNickName().isEmpty()){
      loginResponse = oauthService.loginWithToken(provider, accessToken);
    }
    // 닉네임 변경해서 회원가입시
    else{
      nickName = joinRequest.getNickName();
      log.info("************ nickName : " + nickName);
      loginResponse = oauthService.joinWithToken(provider, accessToken, nickName);
    }

    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(loginResponse));
  }


    @PostMapping("/api/refresh/oauth2")
  public ResponseEntity<TokenResponse> refreshToken(@RequestParam String nickName, @RequestParam String refreshToken) {
    TokenResponse tokenResponse = oauthService.refreshToken(nickName, refreshToken);
    return ResponseEntity.ok().body(tokenResponse);
  }

  @PutMapping ("/api/oauth2/{nickname}")
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
