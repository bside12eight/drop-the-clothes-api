package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.model.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KeepOrDropArticleResponse {

    private Long articleId;

    @JsonInclude(Include.NON_NULL)
    private String category;

    private String title;

    private String description;

    @JsonInclude(Include.NON_NULL)
    private Integer keepCount;

    @JsonInclude(Include.NON_NULL)
    private Integer dropCount;

    @JsonInclude(Include.NON_NULL)
    private String nickname;

    @JsonInclude(Include.NON_NULL)
    private Integer commentCount;

    @JsonInclude(Include.NON_NULL)
    private List<String> images;

    private LocalDateTime createdAt;

    @Builder
    public KeepOrDropArticleResponse(Long articleId, String category, String title,
            String description, Integer keepCount, Integer dropCount, String nickname, Integer commentCount,
            List<String> images, LocalDateTime createdAt) {
        this.articleId = articleId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.keepCount = keepCount;
        this.dropCount = dropCount;
        this.nickname = nickname;
        this.commentCount = commentCount;
        this.images = images;
        this.createdAt = createdAt;
    }

    public static List<KeepOrDropArticleResponse> of(List<Article> articles) {
        List<KeepOrDropArticleResponse> responses = new ArrayList<>();
        for (Article article : articles) {
            KeepOrDropArticleResponse response = KeepOrDropArticleResponse.builder()
                    .articleId(article.getArticleId())
                    .category(article.getCategory().getName())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .createdAt(article.getCreatedAt())
                    .build();

            responses.add(response);
        }

        return responses;
    }
}
