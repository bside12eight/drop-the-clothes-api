package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.Report;
import com.droptheclothes.api.model.enums.ReportType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingBinReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByAddressAndType(String address, ReportType type);
}
