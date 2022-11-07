package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.dto.ReportClothingBinRequest;
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
    private Long reportId;

    @Id
    private Long memberId;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId", insertable = false, updatable = false)
    private Report report;

    public static ReportMember of(Report report, ReportClothingBinRequest request, String uploadPathAndFileName) {
        return ReportMember.builder()
                           .reportId(report.getReportId())
                           .memberId(request.getMemberId())
                           .image(uploadPathAndFileName)
                           .build();
    }

    @Builder
    public ReportMember(Long reportId, Long memberId, String image, Report report) {
        this.reportId = reportId;
        this.memberId = memberId;
        this.image = image;
        this.report = report;
    }
}
