package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.enums.VoteType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    private Category category;

    private String title;

    private String description;

    private int keepCount;

    private int dropCount;

    private int chargedCount;

    @OneToMany(mappedBy = "article")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "article")
    private Set<ArticleImage> articleImages = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static Article of(KeepOrDropArticleRegisterRequest request, Member member, Category category) {
        return Article.builder()
                .member(member)
                .category(category)
                .title(request.getTitle())
                .description(request.getDescription())
                .keepCount(0)
                .dropCount(0)
                .chargedCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Builder
    public Article(Long articleId, Member member, Category category, String title,
            String description, int keepCount, int dropCount, int chargedCount,
            Set<Comment> comments, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        this.articleId = articleId;
        this.member = member;
        this.category = category;
        this.title = title;
        this.description = description;
        this.keepCount = keepCount;
        this.dropCount = dropCount;
        this.chargedCount = chargedCount;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public int vote(VoteType voteType) {
        if (voteType.equals(VoteType.KEEP)) {
            return this.keepCount++;
        } else {
            return this.dropCount++;
        }
    }

    public int cancelVote(VoteType voteType) {
        if (voteType.equals(VoteType.KEEP)) {
            return this.keepCount--;
        } else {
            return this.dropCount--;
        }
    }
}
