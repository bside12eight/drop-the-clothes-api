package com.droptheclothes.api.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ReportImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reportImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "reportId", referencedColumnName = "reportId"),
            @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    })
    private ReportMember reportMember;

    private String filepath;

    public static ReportImage of(ReportMember reportMember, String filepath) {
        return ReportImage.builder()
                          .reportMember(reportMember)
                          .filepath(filepath)
                          .build();
    }

    @Builder
    public ReportImage(Long reportImageId, ReportMember reportMember, String filepath) {
        this.reportImageId = reportImageId;
        this.reportMember = reportMember;
        this.filepath = filepath;
    }
}
