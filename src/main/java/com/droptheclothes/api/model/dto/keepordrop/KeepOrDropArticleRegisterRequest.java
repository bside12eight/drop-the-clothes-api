package com.droptheclothes.api.model.dto.keepordrop;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class KeepOrDropArticleRegisterRequest {

    private String category;

    private String title;

    private String description;

    public KeepOrDropArticleRegisterRequest(String category, String title, String description) {
        this.category = category;
        this.title = title;
        this.description = description;
    }

    public boolean checkArgumentValidation() {
        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("카테고리를 설정해주세요.");
        }

        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("제목을 설정해주세요.");
        }

        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("상세정보를 입력해주세요.");
        }
        return true;
    }
}
