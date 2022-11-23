package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.ClothingBinReportService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ClothingBinReportController {

    private final int MAX_IMAGE_COUNT = 3;

    private final ClothingBinReportService clothingBinReportService;

    @Operation(summary = "신규 의류수거함 제보 API")
    @PostMapping(value = "/api/clothing-bins/report/new",
                 consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse reportNewClothingBin(@RequestPart ClothingBinReportRequest request,
                                            @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        checkImageCountValidation(images);

        clothingBinReportService.reportNewClothingBin(request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @Operation(summary = "변경된 의류수거함 제보 API")
    @PutMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportUpdatedClothingBin(@PathVariable Long clothingBinId,
                                                @RequestPart ClothingBinReportRequest request,
                                                @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        checkImageCountValidation(images);

        clothingBinReportService.reportUpdatedClothingBin(clothingBinId, request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @Operation(summary = "없어진 의류수거함 제보 API")
    @DeleteMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportDeletedClothingBin(@PathVariable Long clothingBinId,
                                                @RequestPart ClothingBinReportRequest request,
                                                @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        checkImageCountValidation(images);

        clothingBinReportService.reportDeletedClothingBin(clothingBinId, request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    private void checkImageCountValidation(List<MultipartFile> images) {
        if (!Objects.isNull(images) && images.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("이미지 파일은 최대 3개까지 업로드 가능합니다.");
        }
    }
}
