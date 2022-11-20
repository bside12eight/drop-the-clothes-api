package com.droptheclothes.api.model.entity;

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
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    private Article article;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private Set<Comment> children = new HashSet<>();

    private int listOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    public Comment(Long commentId, Member member, Article article, String comment,
            Comment parent, Set<Comment> children, int listOrder, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.commentId = commentId;
        this.member = member;
        this.article = article;
        this.comment = comment;
        this.parent = parent;
        this.children = children;
        this.listOrder = listOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
