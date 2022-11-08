package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    private String image;

    public static ReportMember of(Report report, Member member, String uploadPathAndFileName) {
        return ReportMember.builder()
                           .report(report)
                           .member(member)
                           .image(uploadPathAndFileName)
                           .build();
    }

    @Builder
    public ReportMember(Report report, Member member, String image) {
        this.report = report;
        this.member = member;
        this.image = image;
    }
}
