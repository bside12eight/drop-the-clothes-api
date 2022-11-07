package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.ReportClothingBinRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.ClothingBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ClothingBinController {

    private final ClothingBinService clothingBinService;

    @GetMapping("/api/clothing-bins")
    public ApiResponse getClothingBinsWithin1km(Double latitude, Double longitude) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new CollectionObject<>(clothingBinService.getClothingBinsWithin1km(latitude, longitude, 1000)));
    }

    @GetMapping("/api/clothing-bins/{clothingBinId}")
    public ApiResponse getClothingBin(@PathVariable Long clothingBinId) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new SingleObject<>(clothingBinService.getClothingBin(clothingBinId)));
    }

    @PostMapping(value = "/api/clothing-bins/report/new", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse reportNewClothingBin(@RequestPart ReportClothingBinRequest request,
                                            @RequestPart(required = false) MultipartFile image) {
        clothingBinService.reportNewClothingBin(request, image);
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
