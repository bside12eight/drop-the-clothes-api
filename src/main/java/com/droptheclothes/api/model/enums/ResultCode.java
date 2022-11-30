package com.droptheclothes.api.model.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(0, "success"),
    BAD_REQUEST(400, "Invalid syntax for this request was provided."),
    UNAUTHORIZED(401, "You are unauthorized for this request."),
    FORBIDDEN(403, "The server understood the request but refuses to authorize it."),
    NOT_FOUND(404, "We could not find the resource you requested."),
    INTERNAL_SERVER_ERROR(500, "Unexpected internal server error.");

    private int code;

    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
