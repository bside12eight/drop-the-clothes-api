package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@IdClass(ReportMemberId.class)
@NoArgsConstructor
public class ReportMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId", referencedColumnName = "reportId")
    private Report report;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reportMember")
    private Set<ReportImage> reportImage = new HashSet<>();

    public static ReportMember of(Report report, Member member) {
        return ReportMember.builder()
                           .report(report)
                           .member(member)
                           .build();
    }

    @Builder
    public ReportMember(Report report, Member member, Set<ReportImage> reportImage) {
        this.report = report;
        this.member = member;
        this.reportImage = reportImage;
    }
}
