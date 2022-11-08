package com.droptheclothes.api.model.base;

import com.droptheclothes.api.model.enums.ResultCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseHeader {

    private int resultCode;

    private String resultMessage;

    public static ApiResponseHeader create(ResultCode resultCode) {
        return builder().resultCode(resultCode.getCode())
                        .resultMessage(resultCode.getMessage())
                        .build();
    }

    public static ApiResponseHeader create(ResultCode resultCode, String resultMessage) {
        return builder().resultCode(resultCode.getCode())
                        .resultMessage(resultMessage)
                        .build();
    }

    @Builder
    public ApiResponseHeader(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}
