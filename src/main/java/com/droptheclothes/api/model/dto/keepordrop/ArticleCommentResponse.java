package com.droptheclothes.api.model.dto.keepordrop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleCommentResponse {

    private Long commentId;

    private String nickname;

    private String comment;

    @JsonInclude(Include.NON_NULL)
    private List<ArticleCommentResponse> children;

    private LocalDateTime createdAt;

    @Builder
    public ArticleCommentResponse(Long commentId, String nickname, String comment,
            List<ArticleCommentResponse> children, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.nickname = nickname;
        this.comment = comment;
        this.children = children;
        this.createdAt = createdAt;
    }

    public void setChildren(List<ArticleCommentResponse> children) {
        this.children = children;
    }
}
