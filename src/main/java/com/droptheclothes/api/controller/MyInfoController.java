package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.myinfo.MyInfoResponse;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.repository.ClothingBinReportRepository;
import com.droptheclothes.api.service.MyInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyInfoController {

    private final MyInfoService myInfoService;

    private final ClothingBinReportRepository clothingBinReportRepository;

    @GetMapping("/api/my/info")
    public ApiResponse getMyInfo() {
        MyInfoResponse response = myInfoService.getMyInfo();
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(response));
    }

    @PutMapping("/api/my/info/nickname")
    public ApiResponse updateNickname(@RequestParam(required = false) String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }
        myInfoService.updateNickname(nickname);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @PutMapping("/api/my/info/password")
    public ApiResponse updatePassword(@RequestParam(required = false) String currentPassword,
                                      @RequestParam(required = false) String password) {
        if (StringUtils.isBlank(currentPassword)) {
            throw new IllegalArgumentException("현재 비밀번호를 입력해주세요.");
        }

        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("변경할 비밀번호를 입력해주세요.");
        }

        myInfoService.updatePassword(currentPassword, password);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/my/clothing-bin/report")
    public ApiResponse getMyClothingBinReports() {
        List<ClothingBinReportResponse> response = myInfoService.getMyClothingBinReports();
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @GetMapping("/api/my/keep-or-drop/article")
    public ApiResponse getMyKeepOrDropArticles() {
        List<KeepOrDropArticleResponse> response = myInfoService.getMyKeepOrDropArticles();
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }
}
