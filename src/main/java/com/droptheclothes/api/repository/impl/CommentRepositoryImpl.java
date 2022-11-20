package com.droptheclothes.api.repository.impl;

import com.droptheclothes.api.model.entity.QArticle;
import com.droptheclothes.api.model.entity.QComment;
import com.droptheclothes.api.repository.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getCommentListOrder(Long articleId, Long parentId) {
        QArticle article = QArticle.article;
        QComment comment = QComment.comment1;

        return new JPAQueryFactory(entityManager)
                .select(comment.count())
                .from(article)
                .innerJoin(comment)
                .on(comment.article.eq(article)
                .and(comment.parent.isNull()))
                .where(article.articleId.eq(articleId))
                .fetchOne();
    }
}
