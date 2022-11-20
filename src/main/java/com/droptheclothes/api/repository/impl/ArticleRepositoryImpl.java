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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Article> getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
        // TODO: 조회 조건 고도화 필요함
        QArticle article = QArticle.article;

        OrderSpecifier<?> orderSpecifier = null;
        if (request.getOrderType().equals(OrderType.LATEST)) {
            orderSpecifier = article.createdAt.desc();
        }

        Predicate where = null;
        if (!StringUtils.isBlank(request.getCategory())) {
            where = ExpressionUtils.and(where, article.category.name.eq(request.getCategory()));
        }

        return new JPAQueryFactory(entityManager)
                .select(article)
                .from(article)
                .where(where)
                .orderBy(orderSpecifier)
                .fetch();
    }
}
