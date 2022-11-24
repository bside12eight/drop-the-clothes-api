package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.BlockedMember;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.pk.BlockedMemberId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedMemberRepository extends JpaRepository<BlockedMember, BlockedMemberId>  {

    List<BlockedMember> findByMemberOrderByCreatedAtDesc(Member member);
}
