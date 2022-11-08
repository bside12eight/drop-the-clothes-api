package com.droptheclothes.api.model.dto.keepordrop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleCommentResponse {

    private String nickname;

    private String comment;

    @JsonInclude(Include.NON_NULL)
    private List<ArticleCommentResponse> children;

    private LocalDateTime createdAt;

    @Builder
    public ArticleCommentResponse(String nickname, String comment,
            List<ArticleCommentResponse> children, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.comment = comment;
        this.children = children;
        this.createdAt = createdAt;
    }
}
