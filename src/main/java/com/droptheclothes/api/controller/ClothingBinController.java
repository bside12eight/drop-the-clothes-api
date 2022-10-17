package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.ClothingBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClothingBinController {

    private final ClothingBinService clothingBinService;

    @GetMapping("/api/clothing-bins")
    public ApiResponse getClothingBins(Double latitude, Double longitude) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(clothingBinService.getClothingBins(latitude, longitude)));
    }
}