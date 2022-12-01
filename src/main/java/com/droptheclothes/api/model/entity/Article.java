package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.enums.VoteType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(columnDefinition = "TEXT")
    private String description;

    private int keepCount;

    private int dropCount;

    private int commentCount;

    private double popularity;

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
                .commentCount(0)
                .chargedCount(0)
                .popularity(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void vote(VoteType voteType) {
        if (voteType.equals(VoteType.KEEP)) {
            this.keepCount++;
        } else {
            this.dropCount++;
        }
        updatePopularity();
    }

    public void cancelVote(VoteType voteType) {
        if (voteType.equals(VoteType.KEEP)) {
            this.keepCount--;
        } else {
            this.dropCount--;
        }
        updatePopularity();
    }

    public void addComment() {
        this.commentCount++;
        updatePopularity();
    }

    public void removeComment() {
        this.commentCount--;
        updatePopularity();
    }

    public void addCharge() {
        this.chargedCount++;
    }

    private void updatePopularity() {
        this.popularity = ((this.keepCount + this.dropCount) * 0.3) + (this.commentCount * 0.7);
    }
}
