package com.droptheclothes.api.model.base;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SingleObject<T> implements ApiResponseBody {

    private T content;

    public SingleObject(T content) {
        this.content = content;
    }
}