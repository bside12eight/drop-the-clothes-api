package com.droptheclothes.api.exception;

import java.util.ArrayList;
import java.util.List;

public class DropTheClothesApiException extends RuntimeException {

    private ResponseCode responseCode;
    private final List<DropTheClothesApiException> nestedExceptions = new ArrayList<>();

    public DropTheClothesApiException(String message) {
        super(message);
    }

    public DropTheClothesApiException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
