package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemberRepository extends JpaRepository<ReportMember, ReportMemberId> {

}
