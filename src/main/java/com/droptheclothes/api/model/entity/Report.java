package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.dto.ReportClothingBinRequest;
import com.droptheclothes.api.model.enums.ReportStatus;
import com.droptheclothes.api.model.enums.ReportType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    private Long clothingBinId;

    private String address;

    private String detailedAddress;

    private double latitude;

    private double longitude;

    private String comment;

    private int reportCount;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reportId")
    private List<ReportMember> reportMembers = new ArrayList<>();

    public static Report of(ReportClothingBinRequest request) {
        return Report.builder()
                     .type(ReportType.NEW)
                     .address(request.getAddress())
                     .detailedAddress(request.getDetailedAddress())
                     .latitude(request.getLatitude())
                     .longitude(request.getLongitude())
                     .comment(request.getComment())
                     .reportCount(0)
                     .status(ReportStatus.PENDING)
                     .createdAt(LocalDateTime.now())
                     .updatedAt(LocalDateTime.now())
                     .build();
    }

    @Builder
    public Report(Long reportId, ReportType type, Long clothingBinId, String address, String detailedAddress, double latitude,
                  double longitude, String comment, int reportCount, ReportStatus status, LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.reportId = reportId;
        this.type = type;
        this.clothingBinId = clothingBinId;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.comment = comment;
        this.reportCount = reportCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addReportCount() {
        this.reportCount++;
    }
}
