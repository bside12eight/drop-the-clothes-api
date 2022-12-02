package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByNickName(String nickName);

    Optional<Member> findByMemberIdAndIsRemoved(String memberId, boolean isRemoved);
}
