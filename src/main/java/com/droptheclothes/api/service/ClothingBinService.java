package com.droptheclothes.api.service;

import com.droptheclothes.api.exception.DropTheClothesApiException;
import com.droptheclothes.api.model.dto.ClothingBinResponse;
import com.droptheclothes.api.model.dto.NewClothingBinRequest;
import com.droptheclothes.api.model.entity.ClothingBin;
import com.droptheclothes.api.model.entity.Report;
import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import com.droptheclothes.api.model.enums.ReportType;
import com.droptheclothes.api.repository.ClothingBinRepository;
import com.droptheclothes.api.repository.ReportMemberRepository;
import com.droptheclothes.api.repository.ReportRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClothingBinService {

    private final ObjectStorageService objectStorageService;

    private final ClothingBinRepository clothingBinRepository;

    private final ReportRepository reportRepository;

    private final ReportMemberRepository reportMemberRepository;

    public List<ClothingBinResponse> getClothingBinsWithin1km(Double latitude, Double longitude, Integer distance) {
        return clothingBinRepository.getClothingBinsWithin1km(latitude, longitude, distance);
    }

    public ClothingBinResponse getClothingBin(Long clothingBinId) {
        ClothingBin clothingBin = clothingBinRepository.findByClothingBinId(clothingBinId)
                                                       .orElseThrow(() -> new IllegalArgumentException("There is no matched clothing bin"));
        return ClothingBinResponse.entityToDto(clothingBin);
    }

    @Transactional
    public void reportNewClothingBin(NewClothingBinRequest request, MultipartFile image) {
        clothingBinRepository.findByAddress(request.getAddress()).ifPresent(clothingBin -> {
            throw new IllegalArgumentException("해당 위치에는 이미 등록된 의류수거함이 존재합니다.");
        });

        Report report = reportRepository.findByAddressAndType(request.getAddress(), ReportType.NEW)
                                        .orElse(Report.of(request));

        reportMemberRepository.findById(new ReportMemberId(report.getReportId(), request.getMemberId()))
                              .ifPresent(reportMember -> {
                                  throw new DropTheClothesApiException("이미 요청한 이력이 존재합니다.");
                              });
        report.addReportCount();
        reportRepository.save(report);

        String uploadPathAndFileName = null;
        if (!Objects.isNull(image)) {
            final String DIRECTORY = "report/" + report.getReportId().toString();
            uploadPathAndFileName = objectStorageService.uploadFileToObjectStorage(DIRECTORY, image);
        }
        reportMemberRepository.save(ReportMember.of(report, request, uploadPathAndFileName));
    }
}
