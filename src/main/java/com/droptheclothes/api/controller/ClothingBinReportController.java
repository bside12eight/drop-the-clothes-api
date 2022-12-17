package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.security.LoginCheck;
import com.droptheclothes.api.service.ClothingBinReportService;
import com.droptheclothes.api.utility.BusinessConstants;
import com.droptheclothes.api.utility.MessageConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClothingBinReportController {

    private final ClothingBinReportService clothingBinReportService;

    @LoginCheck
    @Operation(summary = "신규 의류수거함 제보 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PostMapping(value = "/api/clothing-bins/report/new",
                 consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse reportNewClothingBin(@RequestPart ClothingBinReportRequest request,
                                            @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        checkImageCountValidation(images);
        clothingBinReportService.reportNewClothingBin(request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @LoginCheck
    @Operation(summary = "변경된 의류수거함 제보 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PutMapping("/api/clothing-bins/report/{clothingBinId}")
    public ApiResponse reportUpdatedClothingBin(@PathVariable Long clothingBinId,
                                                @RequestPart ClothingBinReportRequest request,
                                                @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        checkImageCountValidation(images);

        clothingBinReportService.reportUpdatedClothingBin(clothingBinId, request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @LoginCheck
    @Operation(summary = "없어진 의류수거함 제보 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
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
        if (!Objects.isNull(images) && images.size() > BusinessConstants.MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(String.format(MessageConstants.MAX_IMAGE_COUNT_EXCEED_MESSAGE, BusinessConstants.MAX_IMAGE_COUNT));
        }
    }
}
