package com.droptheclothes.api.repository.impl;

import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.QArticle;
import com.droptheclothes.api.model.enums.OrderType;
import com.droptheclothes.api.repository.ArticleRepositoryCustom;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Article> getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
        final int PAGE_SIZE = 30;

        QArticle article = QArticle.article;

        OrderSpecifier<?> orderSpecifier = null;
        if (request.getOrderType().equals(OrderType.LATEST)) {
            orderSpecifier = article.createdAt.desc();
        } else if (request.getOrderType().equals(OrderType.POPULARITY)) {
            orderSpecifier = article.popularity.desc();
        }

        Predicate where = null;
        if (!Objects.isNull(request.getCategories()) && !request.getCategories().contains("전체")) {
            where = ExpressionUtils.and(where, article.category.name.in(request.getCategories()));
        }

        return new JPAQueryFactory(entityManager)
                .select(article)
                .from(article)
                .where(where)
                .offset(request.getOffset(PAGE_SIZE))
                .limit(PAGE_SIZE)
                .orderBy(orderSpecifier)
                .fetch();
    }
}
