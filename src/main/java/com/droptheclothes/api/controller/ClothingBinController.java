package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.ClothingBinService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClothingBinController {

    private final ClothingBinService clothingBinService;

    @Operation(summary = "반경 1KM 이내 의류수거함 조회 API")
    @GetMapping("/api/clothing-bins")
    public ApiResponse getClothingBinsWithin1km(Double latitude, Double longitude) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new CollectionObject<>(clothingBinService.getClothingBinsWithin1km(latitude, longitude, 1000)));
    }

    @Operation(summary = "의류수거함 상세 조회 API")
    @GetMapping("/api/clothing-bins/{clothingBinId}")
    public ApiResponse getClothingBin(@PathVariable Long clothingBinId) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new SingleObject<>(clothingBinService.getClothingBin(clothingBinId)));
    }
}
