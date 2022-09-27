package com.droptheclothes.api.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;

@Getter
public class CollectionObject<T> implements ApiResponseBody {

    private List<T> contents;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer totalCount;

    public CollectionObject(List<T> contents) {
        this.contents = contents;
        this.totalCount = contents == null ? null : contents.size();
    }
}
