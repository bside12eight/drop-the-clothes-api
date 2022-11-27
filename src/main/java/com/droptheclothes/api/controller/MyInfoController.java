package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.myinfo.BlockedUserResponse;
import com.droptheclothes.api.model.dto.myinfo.MyInfoResponse;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.security.LoginCheck;
import com.droptheclothes.api.service.MyInfoService;
import com.droptheclothes.api.utility.MessageConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyInfoController {

    private final MyInfoService myInfoService;

    @LoginCheck
    @Operation(summary = "내 정보 조회 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @GetMapping("/api/my/info")
    public ApiResponse<MyInfoResponse> getMyInfo() {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(myInfoService.getMyInfo()));
    }

    @LoginCheck
    @Operation(summary = "닉네임 변경 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PutMapping("/api/my/info/nickname")
    public ApiResponse updateNickname(@RequestParam(required = false) String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        myInfoService.updateNickname(nickname);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @LoginCheck
    @Operation(summary = "비밀번호 변경 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PutMapping("/api/my/info/password")
    public ApiResponse updatePassword(@RequestParam(required = false) String currentPassword,
                                      @RequestParam(required = false) String password) {
        if (StringUtils.isBlank(currentPassword) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        myInfoService.updatePassword(currentPassword, password);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @LoginCheck
    @Operation(summary = "나의 의류수거함 제보 목록 조회 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @GetMapping("/api/my/clothing-bin/report")
    public ApiResponse<List<ClothingBinReportResponse>> getMyClothingBinReports() {
        List<ClothingBinReportResponse> response = myInfoService.getMyClothingBinReports();
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @LoginCheck
    @Operation(summary = "나의 버릴까 말까 글 목록 조회 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @GetMapping("/api/my/keep-or-drop/article")
    public ApiResponse<List<KeepOrDropArticleResponse>> getMyKeepOrDropArticles() {
        List<KeepOrDropArticleResponse> response = myInfoService.getMyKeepOrDropArticles();
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @LoginCheck
    @Operation(summary = "유저 차단하기 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PostMapping("/api/my/block/user")
    public ApiResponse blockUser(String nickName) {
        myInfoService.blockUser(nickName);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @LoginCheck
    @Operation(summary = "차단한 유저 목록 조회 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @GetMapping("/api/my/block/user")
    public ApiResponse<List<BlockedUserResponse>> getBlockedUsers() {
        List<BlockedUserResponse> response = myInfoService.getBlockedUsers();
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @LoginCheck
    @Operation(summary = "유저 차단해제 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @DeleteMapping("/api/my/block/user")
    public ApiResponse unblockUser(String memberId) {
        myInfoService.unblockUser(memberId);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }
}
