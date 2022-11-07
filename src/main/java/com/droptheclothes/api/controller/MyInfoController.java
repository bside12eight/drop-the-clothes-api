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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyInfoController {

    private final ClothingBinReportRepository clothingBinReportRepository;

    @GetMapping("/api/my/info")
    public ApiResponse getMyInfo() {
        MyInfoResponse dummyResponse = new MyInfoResponse("dummy@gmail.com", "dummyNickname", "www.profileimage.com");
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(dummyResponse));
    }

    @PutMapping("/api/my/info")
    public ApiResponse updateMyInfo(@RequestParam String nickname) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @PutMapping("/api/my/info/password")
    public ApiResponse updatePassword(@RequestParam String password) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/my/clothing-bin/report")
    public ApiResponse getMyClothingBinReports() {
        List<ClothingBinReportResponse> dummyResponse = clothingBinReportRepository.findAll()
                .stream()
                .map(ClothingBinReportResponse::entityToDto)
                .collect(Collectors.toList());

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(dummyResponse));
    }

    @GetMapping("/api/my/keep-or-drop/article")
    public ApiResponse getMyKeepOrDropArticles() {
        List<KeepOrDropArticleResponse> dummyResponse = new ArrayList<>();
        dummyResponse.add(new KeepOrDropArticleResponse("title1", "desc1", LocalDateTime.now()));
        dummyResponse.add(new KeepOrDropArticleResponse("title2", "desc2", LocalDateTime.now()));
        dummyResponse.add(new KeepOrDropArticleResponse("title3", "desc3", LocalDateTime.now()));

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(dummyResponse));
    }
}
