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
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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

        checkAlreadyExistClothingBin(request);

        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        Report report = clothingBinReportRepository.findByAddressAndType(request.getAddress(), ReportType.NEW)
                                        .orElse(Report.of(request, coordinate, ReportType.NEW));

        checkDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()));

        updateReportCount(report);

        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        uploadReportImages(images, report, reportMember);

        registerClothingBinReported3Times(report);
    }

    @Transactional
    public void reportUpdatedClothingBin(Long clothingBinId, ClothingBinReportRequest request, List<MultipartFile> images) {
        Coordinate coordinate = geocodingService.findCoordinateByAddress(request.getAddress());

        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        ClothingBin clothingBin = clothingBinRepository.findById(clothingBinId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.MATCHED_CLOTHING_BIN_NOT_FOUND_MESSAGE));

        Report report = clothingBinReportRepository.findByClothingBinAndType(clothingBin, ReportType.UPDATE)
                .orElse(Report.of(clothingBin, request, coordinate, ReportType.UPDATE));

        checkDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()));

        updateReportCount(report);

        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        uploadReportImages(images, report, reportMember);

        updateClothingBinReported3Times(clothingBin, report);
    }

    @Transactional
    public void reportDeletedClothingBin(Long clothingBinId, ClothingBinReportRequest request, List<MultipartFile> images) {
        Coordinate coordinate = geocodingService.findCoordinateByAddress(request.getAddress());

        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        ClothingBin clothingBin = clothingBinRepository.findById(clothingBinId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.MATCHED_CLOTHING_BIN_NOT_FOUND_MESSAGE));

        Report report = clothingBinReportRepository.findByClothingBinAndType(clothingBin, ReportType.DELETE)
                .orElse(Report.of(clothingBin, request, coordinate, ReportType.DELETE));

        checkDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()));

        updateReportCount(report);

        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        uploadReportImages(images, report, reportMember);

        deleteClothingBinReported3Times(clothingBin, report);
    }

    private void checkAlreadyExistClothingBin(ClothingBinReportRequest request) {
        if (clothingBinRepository.findByAddress(request.getAddress()).isPresent()) {
            throw new IllegalArgumentException(MessageConstants.ALREADY_EXIST_CLOTHING_BIN_MESSAGE);
        }
    }

    private boolean checkDuplicatedReport(ReportMemberId reportMemberId) {
        if (reportMemberRepository.findById(reportMemberId).isPresent()) {
            throw new IllegalArgumentException(MessageConstants.DUPLICATED_REPORT_MESSAGE);
        }
        return false;
    }

    private void updateReportCount(Report report) {
        report.addReportCount();
        clothingBinReportRepository.save(report);
    }

    private void uploadReportImages(List<MultipartFile> images, Report report, ReportMember reportMember) {
        if (!Objects.isNull(images)) {
            storeClothingBinReportImages(report, images).stream()
                    .forEach(filepath -> reportImageRepository.save(ReportImage.of(reportMember, filepath)));
        }
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

    private String getFirstImage(Report report) {
        ReportMember reportMember = report.getReportMembers().iterator().next();
        Iterator<ReportImage> reportImageIterator = reportMember.getReportImage().iterator();
        if (!reportImageIterator.hasNext()) {
            return null;
        }
        return reportImageIterator.next().getFilepath();
    }

    private boolean registerClothingBinReported3Times(Report report) {
        if (report.getReportCount() < BusinessConstants.MAX_REPORT_COUNT) {
            return false;
        }
        ClothingBin clothingBin = ClothingBin.builder()
                .address(report.getAddress())
                .detailedAddress(report.getDetailedAddress())
                .latitude(report.getLatitude())
                .longitude(report.getLongitude())
                .image(getFirstImage(report))
                .active(true)
                .chargedCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        clothingBinRepository.save(clothingBin);
        return true;
    }

    private boolean updateClothingBinReported3Times(ClothingBin clothingBin, Report report) {
        if (report.getReportCount() < BusinessConstants.MAX_REPORT_COUNT) {
            return false;
        }
        String firstImagePath = getFirstImage(report);
        clothingBin.updateClothingBin(report, firstImagePath);
        clothingBinRepository.save(clothingBin);
        return true;
    }

    private boolean deleteClothingBinReported3Times(ClothingBin clothingBin, Report report) {
        if (report.getReportCount() < BusinessConstants.MAX_REPORT_COUNT) {
            return false;
        }
        clothingBin.inactiveClothingBin();
        return true;
    }
}
