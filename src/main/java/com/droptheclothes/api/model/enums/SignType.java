package com.droptheclothes.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignType {
    SIGNIN("sign-in", "로그인"),
    SIGNUP("sign-up", "회원가입");

    private final String type;
    private final String name;
}
