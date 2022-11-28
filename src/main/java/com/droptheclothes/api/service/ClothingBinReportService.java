package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportRequest;
import com.droptheclothes.api.model.dto.geocoding.Coordinate;
import com.droptheclothes.api.model.entity.ClothingBin;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.Report;
import com.droptheclothes.api.model.entity.ReportImage;
import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import com.droptheclothes.api.model.enums.ReportType;
import com.droptheclothes.api.repository.ClothingBinReportRepository;
import com.droptheclothes.api.repository.ClothingBinRepository;
import com.droptheclothes.api.repository.ReportImageRepository;
import com.droptheclothes.api.repository.ReportMemberRepository;
import com.droptheclothes.api.security.SecurityUtility;
import com.droptheclothes.api.utility.BusinessConstants;
import com.droptheclothes.api.utility.MessageConstants;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClothingBinReportService {

    private final MemberService memberService;

    private final GeocodingService geocodingService;

    private final ObjectStorageService objectStorageService;

    private final ClothingBinRepository clothingBinRepository;

    private final ClothingBinReportRepository clothingBinReportRepository;

    private final ReportMemberRepository reportMemberRepository;

    private final ReportImageRepository reportImageRepository;

    @Transactional
    public void reportNewClothingBin(ClothingBinReportRequest request, List<MultipartFile> images) {
        Coordinate coordinate = geocodingService.findCoordinateByAddress(request.getAddress());

        if (isAlreadyRegisteredAddress(request.getAddress())) {
            throw new IllegalArgumentException("해당 위치에는 이미 등록된 의류수거함이 존재합니다.");
        }
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        Report report = clothingBinReportRepository.findByAddressAndType(request.getAddress(), ReportType.NEW)
                                        .orElse(Report.of(request, coordinate, ReportType.NEW));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException(MessageConstants.DUPLICATED_REPORT_MESSAGE);
        }

        updateReportCount(report);
        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        if (!Objects.isNull(images)) {
            storeClothingBinReportImages(report, images).stream()
                    .forEach(filepath -> reportImageRepository.save(ReportImage.of(reportMember, filepath)));
        }
    }

    @Transactional
    public void reportUpdatedClothingBin(Long clothingBinId, ClothingBinReportRequest request, List<MultipartFile> images) {
        Coordinate coordinate = geocodingService.findCoordinateByAddress(request.getAddress());

        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        ClothingBin clothingBin = clothingBinRepository.findById(clothingBinId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.MATCHED_CLOTHING_BIN_NOT_FOUND_MESSAGE));

        Report report = clothingBinReportRepository.findByClothingBinAndType(clothingBin, ReportType.UPDATE)
                .orElse(Report.of(clothingBin, request, coordinate, ReportType.UPDATE));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException(MessageConstants.DUPLICATED_REPORT_MESSAGE);
        }

        updateReportCount(report);
        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        if (!Objects.isNull(images)) {
            storeClothingBinReportImages(report, images).stream()
                    .forEach(filepath -> reportImageRepository.save(ReportImage.of(reportMember, filepath)));
        }
    }

    @Transactional
    public void reportDeletedClothingBin(Long clothingBinId, ClothingBinReportRequest request, List<MultipartFile> images) {
        Coordinate coordinate = geocodingService.findCoordinateByAddress(request.getAddress());

        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        ClothingBin clothingBin = clothingBinRepository.findById(clothingBinId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.MATCHED_CLOTHING_BIN_NOT_FOUND_MESSAGE));

        Report report = clothingBinReportRepository.findByClothingBinAndType(clothingBin, ReportType.DELETE)
                .orElse(Report.of(clothingBin, request, coordinate, ReportType.DELETE));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException(MessageConstants.DUPLICATED_REPORT_MESSAGE);
        }

        updateReportCount(report);
        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        if (!Objects.isNull(images)) {
            storeClothingBinReportImages(report, images).stream()
                    .forEach(filepath -> reportImageRepository.save(ReportImage.of(reportMember, filepath)));
        }
    }

    public boolean isAlreadyRegisteredAddress(String address) {
        return clothingBinRepository.findByAddress(address).isPresent();
    }

    private boolean isDuplicatedReport(ReportMemberId reportMemberId) {
        return reportMemberRepository.findById(reportMemberId).isPresent();
    }

    private void updateReportCount(Report report) {
        report.addReportCount();
        clothingBinReportRepository.save(report);
    }

    private List<String> storeClothingBinReportImages(Report report, List<MultipartFile> images) {
        if (images.size() > BusinessConstants.MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(String.format(MessageConstants.MAX_IMAGE_COUNT_EXCEED_MESSAGE, BusinessConstants.MAX_IMAGE_COUNT));
        }
        final String DIRECTORY = BusinessConstants.REPORT_IMAGE_DIRECTORY + report.getReportId().toString();

        return images.stream()
                .map(image -> objectStorageService.uploadFileToObjectStorage(DIRECTORY, image))
                .collect(Collectors.toList());
    }
}
