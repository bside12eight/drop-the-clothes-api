package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.dto.ReportClothingBinRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.ClothingBinReportService;
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

    private final ClothingBinReportService clothingBinReportService;

    @PostMapping(value = "/api/clothing-bins/report/new",
                 consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse reportNewClothingBin(@RequestPart ReportClothingBinRequest request,
                                            @RequestPart(required = false) MultipartFile image) {
        clothingBinReportService.reportNewClothingBin(request, image);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @PutMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportUpdatedClothingBin(@PathVariable Long clothingBinId,
                                                @RequestPart ReportClothingBinRequest request,
                                                @RequestPart(required = false) MultipartFile image) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @DeleteMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportDeletedClothingBin(@PathVariable Long clothingBinId) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }
}
