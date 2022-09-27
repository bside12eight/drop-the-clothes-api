package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.CoordinateResponse;
import com.droptheclothes.api.model.enums.ResultCode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordinateController {

    @GetMapping("/api/coordinate")
    public ApiResponse getCoordinate() {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(new CoordinateResponse(123, 231)));
    }

    @GetMapping("/api/coordinates")
    public ApiResponse getCoordinates() {
        List<CoordinateResponse> coordinateResponses = new ArrayList<>();
        coordinateResponses.add(new CoordinateResponse(123, 231));
        coordinateResponses.add(new CoordinateResponse(124, 232));
        coordinateResponses.add(new CoordinateResponse(234, 111));
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(coordinateResponses));
    }
}
