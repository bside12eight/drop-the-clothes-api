package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.entity.Article;
import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request);
}
