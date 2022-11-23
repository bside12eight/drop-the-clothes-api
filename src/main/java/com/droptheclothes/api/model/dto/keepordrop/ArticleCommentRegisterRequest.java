package com.droptheclothes.api.model.dto.keepordrop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ArticleCommentRegisterRequest {

    @Schema(required = true)
    private String comment;

    private Long parentId;

    public boolean checkArgumentValidation() {
        if (StringUtils.isBlank(comment)) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }
        return true;
    }
}
