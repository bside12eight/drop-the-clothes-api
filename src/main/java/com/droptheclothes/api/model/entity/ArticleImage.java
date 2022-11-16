package com.droptheclothes.api.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class ArticleImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long articleImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    private Article article;

    private String filepath;

    public static ArticleImage of(Article article, String filepath) {
        return ArticleImage.builder()
                .article(article)
                .filepath(filepath)
                .build();
    }

    @Builder
    public ArticleImage(Long articleImageId, Article article, String filepath) {
        this.articleImageId = articleImageId;
        this.article = article;
        this.filepath = filepath;
    }
}
