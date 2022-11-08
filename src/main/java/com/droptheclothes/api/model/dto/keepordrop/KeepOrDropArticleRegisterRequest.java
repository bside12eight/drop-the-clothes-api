package com.droptheclothes.api.model.dto.keepordrop;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class KeepOrDropArticleRegisterRequest {

    private String category;

    private String description;

    public boolean checkArgumentValidation() {
        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("카테고리를 설정해주세요.");
        }

        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("상세정보를 입력해주세요.");
        }
        return true;
    }
}
