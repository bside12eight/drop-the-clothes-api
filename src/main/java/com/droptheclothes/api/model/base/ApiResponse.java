package com.droptheclothes.api.model.base;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private ApiResponseHeader header;

    private ApiResponseBody<T> data;

    public ApiResponse(ApiResponseHeader header, ApiResponseBody data) {
        this.header = header;
        this.data = data;
    }

}
