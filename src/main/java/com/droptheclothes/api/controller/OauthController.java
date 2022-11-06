package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.auth.JoinRequest;
import com.droptheclothes.api.model.dto.auth.LoginRequest;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.OauthResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.dto.auth.UpdateRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  /**
   * 최초 회원가입 진입 시, 존재하는 회원인지 판별해주는 api
   * 존재하는 회원일 때에는 로그인 진행
   */
  @PostMapping(value = "/api/oauth2/{provider}")
  public ApiResponse loginWithToken2(@PathVariable String provider, @RequestBody LoginRequest loginRequest) {

    String accessToken = loginRequest.getAccessToken();
    OauthResponse oauthResponse = null;

    oauthResponse = oauthService.checkExistMemberWithToken(provider, accessToken);

    if(oauthResponse.getType().trim().equals("sign-in") ){
      return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
          new SingleObject<>(oauthService.loginWithToken(provider, accessToken)));
    }
    else{
      return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
          new SingleObject<>(oauthResponse));
    }



  }

  /**
   * 회원가입 api
   */
  @PostMapping(value = "/api/oauth2/{provider}/signup")
  public ApiResponse join(@PathVariable String provider, @RequestBody JoinRequest joinRequest) {

    String accessToken = joinRequest.getAccessToken();
    String nickName = "";
    LoginResponse loginResponse = null;

    // 닉네임 변경해서 회원가입시
    if(
        ( joinRequest.getNickName().isEmpty() )
            || (joinRequest.getNickName().equals("") )
            || (joinRequest.getNickName() == null)
    ){
      ;
    }
    else{
      nickName = joinRequest.getNickName();

    }
    loginResponse = oauthService.joinWithToken(provider, accessToken, nickName);

    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(loginResponse));
  }


  /**
   * 닉네임 중복 판별 api
   */
  @GetMapping("/api/oauth2/{nickName}")
  public ApiResponse checkNickName(@RequestParam String nickName) {
    Boolean checkNickName = oauthService.isExistNickName(nickName);
    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(checkNickName));
  }


  /**
   * 닉네임 수정 api
   */
  @PutMapping("/api/oauth2/{memberId}")
  public ApiResponse checkNickName(@PathVariable String memberId, @RequestBody UpdateRequest updateRequest) {
    Boolean changeNickName = oauthService.updateNickName(memberId, updateRequest.getNickName());
    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(changeNickName));
  }

  /**
   * 회원탈퇴 api
   */
  @DeleteMapping("/api/oauth2/{memberId}")
  public ApiResponse deleteProfile(@PathVariable String memberId) {
    Boolean isDelete = oauthService.deleteProfile(memberId);
    return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
        new SingleObject<>(isDelete));
  }



  @PostMapping("/api/refresh/oauth2")
  public ResponseEntity<TokenResponse> refreshToken(@RequestParam String nickName, @RequestParam String refreshToken) {
    TokenResponse tokenResponse = oauthService.refreshToken(nickName, refreshToken);
    return ResponseEntity.ok().body(tokenResponse);
  }


}