package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemberRepository extends JpaRepository<ReportMember, ReportMemberId> {

    List<ReportMember> findByMemberOrderByCreatedAtDesc(Member member);
}
