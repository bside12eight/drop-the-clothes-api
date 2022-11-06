package com.droptheclothes.api.model.base;

import lombok.Getter;

@Getter
public class ApiResponse {

    private ApiResponseHeader header;

    private ApiResponseBody data;

    public ApiResponse(ApiResponseHeader header, ApiResponseBody data) {
        this.header = header;
        this.data = data;
    }

}
