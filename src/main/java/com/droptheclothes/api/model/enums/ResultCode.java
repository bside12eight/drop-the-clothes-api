package com.droptheclothes.api.model.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(0, "success");

    private int code;

    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
