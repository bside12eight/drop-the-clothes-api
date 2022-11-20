package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.Vote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByMemberAndArticle(Member member, Article article);
}
