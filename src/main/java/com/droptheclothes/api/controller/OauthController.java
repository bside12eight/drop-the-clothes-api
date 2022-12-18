package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.auth.JoinRequest;
import com.droptheclothes.api.model.dto.auth.LoginRequest;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.OauthResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.enums.LoginProviderType;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.model.enums.SignType;
import com.droptheclothes.api.security.LoginCheck;
import com.droptheclothes.api.service.AppleAuthenticationService;
import com.droptheclothes.api.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    private final AppleAuthenticationService appleAuthenticationService;

    @PostMapping(value = "/api/oauth2/{provider}")
    @Operation(summary = "최초 회원가입 진입 시, 존재하는 회원인지 판별해주는 api", description = "최초 회원가입 진입 시, 존재하는 회원인지 판별해주는 api")
    public ApiResponse login(@PathVariable LoginProviderType provider, @RequestBody LoginRequest loginRequest) {
        log.debug(String.format("provider: %s", provider));
        log.debug(String.format("accessToken: %s, identityToken: %s", loginRequest.getAccessToken(), loginRequest.getIdentityToken()));

        if (provider.equals(LoginProviderType.apple)) {
            return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                    new SingleObject<>(appleAuthenticationService.login(loginRequest)));
        }

        String accessToken = loginRequest.getAccessToken();
        OauthResponse oauthResponse = oauthService.checkExistMemberWithToken(provider, accessToken);

        if (oauthResponse.getType().trim().equals(SignType.SIGNIN.getType())) {
            return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                    new SingleObject<>(oauthService.loginWithToken(provider, accessToken)));
        } else {
            return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                    new SingleObject<>(oauthResponse));
        }
    }

    @PostMapping(value = "/api/oauth2/{provider}/signup")
    @Operation(summary = "회원가입 api", description = "회원가입 api")
    public ApiResponse signUp(@PathVariable LoginProviderType provider, @RequestBody JoinRequest joinRequest) {
        log.debug(String.format("provider: %s", provider));
        log.debug(String.format("nickName: %s", joinRequest.getNickName()));
        log.debug(String.format("accessToken: %s, identityToken: %s", joinRequest.getAccessToken(), joinRequest.getIdentityToken()));

        if (provider.equals(LoginProviderType.apple)) {
            return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                    new SingleObject<>(appleAuthenticationService.signUp(provider, joinRequest)));
        }

        String accessToken = joinRequest.getAccessToken();
        String nickName = "";
        LoginResponse loginResponse = null;

        // 닉네임 변경해서 회원가입시
        if (
                (joinRequest.getNickName().isEmpty())
                        || (joinRequest.getNickName().equals(""))
                        || (joinRequest.getNickName() == null)
        ) {
            ;
        } else {
            nickName = joinRequest.getNickName();

        }
        loginResponse = oauthService.joinWithToken(provider, accessToken, nickName);

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new SingleObject<>(loginResponse));
    }

    @GetMapping("/api/oauth2/{nickName}")
    @Operation(summary = "닉네임 중복 판별 api", description = "닉네임 중복 판별 api")
    public ApiResponse isNickNameExist(@PathVariable String nickName) {
        Boolean isNickNameExist = oauthService.isNickNameExist(nickName);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(isNickNameExist));
    }

    @LoginCheck
    @DeleteMapping("/api/oauth2/member")
    @Operation(summary = "회원탈퇴 api", description = "회원탈퇴 api")
    public ApiResponse deleteMember(String authorizationCode) {
        oauthService.deleteMember(authorizationCode);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(null));
    }


    @PostMapping("/api/refresh/oauth2")
    @Operation(summary = "사용 금지(작업 중)", description = "토큰 갱신 api")
    public ResponseEntity<TokenResponse> refreshToken(@RequestParam String nickName,
            @RequestParam String refreshToken) {
        TokenResponse tokenResponse = oauthService.refreshToken(nickName, refreshToken);
        return ResponseEntity.ok().body(tokenResponse);
    }
}