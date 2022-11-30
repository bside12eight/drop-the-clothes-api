package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.enums.VoteType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    private Article article;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    private LocalDateTime createdAt;

    @Builder
    public Vote(Long voteId, Member member, Article article, VoteType voteType, LocalDateTime createdAt) {
        this.voteId = voteId;
        this.member = member;
        this.article = article;
        this.voteType = voteType;
        this.createdAt = createdAt;
    }
}
