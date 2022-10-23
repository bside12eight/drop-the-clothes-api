package com.droptheclothes.api.controller;

import com.droptheclothes.api.jwt.JwtHeaderUtil;
import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.dto.UserInfo;
import com.droptheclothes.api.model.dto.UserInfoResponse;
import com.droptheclothes.api.service.MembersService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MembersController {

    private final MembersService membersService;

    /**
     * 현재 로그인한 유저의 정보 조회
     * @param request
     * @return ResponseEntity<UserInfoResponse>
     */
    //@ApiOperation(value = "로그인한 유저의 정보 조회", notes = "로그인한 유저의 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        String token = JwtHeaderUtil.getAccessToken(request);
        return ApiResponse.success(membersService.getUserInfo(token));
    }

    /**
     * 현재 로그인한 유저의 프로필이미지 & 닉네임 변경 기능
     * @param request
     * @param userInfo
     */
    //@ApiOperation(value = "현재 로그인한 유저의 프로필이미지 & 닉네임 변경 기능", notes = "현재 로그인한 유저의 프로필이미지 & 닉네임 변경 기능")
    @PutMapping("/info")
    public ResponseEntity<UserInfo> updateUserInfo(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        String token = JwtHeaderUtil.getAccessToken(request);
        return ApiResponse.success(membersService.updateUserInfo(token, userInfo));
    }
}
