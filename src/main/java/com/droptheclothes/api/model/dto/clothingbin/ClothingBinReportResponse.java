package com.droptheclothes.api.model.dto.clothingbin;

import com.droptheclothes.api.model.entity.Report;
import com.droptheclothes.api.model.enums.ReportStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothingBinReportResponse {

    private String address;

    private ReportStatus status;

    private LocalDateTime createdAt;

    public static ClothingBinReportResponse entityToDto(Report report) {
        return new ClothingBinReportResponse(report.getAddress(), report.getStatus(), report.getCreatedAt());
    }
}
