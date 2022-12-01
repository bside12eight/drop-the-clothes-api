package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.utility.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ArticleCommentRegisterRequest {

    @Schema(required = true)
    private String comment;

    private Long parentId;

    public ArticleCommentRegisterRequest(String comment, Long parentId) {
        if (StringUtils.isBlank(comment)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        this.comment = comment;
        this.parentId = parentId;
    }
}
