package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.utility.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class KeepOrDropArticleRegisterRequest {

    @Schema(required = true)
    private String category;

    @Schema(required = true)
    private String title;

    @Schema(required = true)
    private String description;

    public KeepOrDropArticleRegisterRequest(String category, String title, String description) {
        if (StringUtils.isBlank(category) || StringUtils.isBlank(title) || StringUtils.isBlank(description)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        this.category = category;
        this.title = title;
        this.description = description;
    }
}
