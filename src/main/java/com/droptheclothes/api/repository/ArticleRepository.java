package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findByMemberOrderByCreatedAtDesc(Member member);
}
