package com.droptheclothes.api.exception;

import lombok.Getter;

@Getter
public enum ResponseCode {

    INVALID_INPUT("001", "입력 값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED("003", "허용되지 않은 요청 방법입니다."),
    EXIST_MEMBER("101", "이미 존재하는 회원입니다."),
    EXIST_NICKNAME("102", "이미 존재하는 닉네임입니다.");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
