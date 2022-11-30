package com.droptheclothes.api.model.dto.clothingbin;

import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.enums.ReportStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClothingBinReportResponse {

    private String address;

    private ReportStatus status;

    private LocalDateTime createdAt;

    public static List<ClothingBinReportResponse> of(List<ReportMember> reportMembers) {
        return reportMembers.stream().map(reportMember -> ClothingBinReportResponse.builder()
                        .address(reportMember.getReport().getAddress())
                        .status(reportMember.getReport().getStatus())
                        .createdAt(reportMember.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
