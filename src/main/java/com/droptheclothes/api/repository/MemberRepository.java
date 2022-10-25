package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Member findByEmail(String email);
}
