package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Members, Long> {

    Members findMemberById(Long memberId);
}
