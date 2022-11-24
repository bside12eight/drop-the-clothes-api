package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
  Member findByEmail(String email);
  Member findByMemberId(String memberId);
  Member findByNickName(String nickName);
}
