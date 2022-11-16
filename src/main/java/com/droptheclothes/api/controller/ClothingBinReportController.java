package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.ClothingBinReportService;
import java.util.List;
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
    public ApiResponse reportNewClothingBin(@RequestPart ClothingBinReportRequest request,
                                            @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        clothingBinReportService.reportNewClothingBin(request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @PutMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportUpdatedClothingBin(@PathVariable Long clothingBinId,
                                                @RequestPart ClothingBinReportRequest request,
                                                @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        clothingBinReportService.reportUpdatedClothingBin(clothingBinId, request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @DeleteMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportDeletedClothingBin(@PathVariable Long clothingBinId,
                                                @RequestPart ClothingBinReportRequest request,
                                                @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        clothingBinReportService.reportDeletedClothingBin(clothingBinId, request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }
}
