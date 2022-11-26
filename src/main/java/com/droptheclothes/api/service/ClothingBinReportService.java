package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClothingBinReportService {

    private final MemberService memberService;

    private final ClothingBinService clothingBinService;

    private final ObjectStorageService objectStorageService;

    private final ClothingBinRepository clothingBinRepository;

    private final ClothingBinReportRepository clothingBinReportRepository;

    private final ReportMemberRepository reportMemberRepository;

    private final ReportImageRepository reportImageRepository;

    @Transactional
    public void reportNewClothingBin(ClothingBinReportRequest request, List<MultipartFile> images) {
        if (clothingBinService.isRegisteredClothingBin(request.getAddress())) {
            throw new IllegalArgumentException("해당 위치에는 이미 등록된 의류수거함이 존재합니다.");
        }
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        Report report = clothingBinReportRepository.findByAddressAndType(request.getAddress(), ReportType.NEW)
                                        .orElse(Report.of(request, ReportType.NEW));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException("이미 요청한 이력이 존재합니다.");
        }

        updateReportCountAndSaveClothingBinReport(report);
        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        if (!Objects.isNull(images)) {
            List<String> uploadPathAndFileName = storeClothingBinReportImages(report, images);
            uploadPathAndFileName.stream().forEach(filepath -> {
                reportImageRepository.save(ReportImage.of(reportMember, filepath));
            });
        }
    }

    @Transactional
    public void reportUpdatedClothingBin(Long clothingBinId, ClothingBinReportRequest request, List<MultipartFile> images) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        ClothingBin clothingBin = clothingBinRepository.findById(clothingBinId)
                .orElseThrow(() -> new IllegalArgumentException("제보 대상 의류수거함 정보가 올바르지 않습니다."));

        Report report = clothingBinReportRepository.findByClothingBinAndType(clothingBin, ReportType.UPDATE)
                .orElse(Report.of(clothingBin, request, ReportType.UPDATE));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException("이미 요청한 이력이 존재합니다.");
        }

        updateReportCountAndSaveClothingBinReport(report);
        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        if (!Objects.isNull(images)) {
            List<String> uploadPathAndFileName = storeClothingBinReportImages(report, images);
            uploadPathAndFileName.stream().forEach(filepath -> {
                reportImageRepository.save(ReportImage.of(reportMember, filepath));
            });
        }
    }

    @Transactional
    public void reportDeletedClothingBin(Long clothingBinId, ClothingBinReportRequest request, List<MultipartFile> images) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        ClothingBin clothingBin = clothingBinRepository.findById(clothingBinId)
                .orElseThrow(() -> new IllegalArgumentException("제보 대상 의류수거함 정보가 올바르지 않습니다."));

        Report report = clothingBinReportRepository.findByClothingBinAndType(clothingBin, ReportType.DELETE)
                .orElse(Report.of(clothingBin, request, ReportType.DELETE));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException("이미 요청한 이력이 존재합니다.");
        }

        updateReportCountAndSaveClothingBinReport(report);
        ReportMember reportMember = reportMemberRepository.save(ReportMember.of(report, member));

        if (!Objects.isNull(images)) {
            List<String> uploadPathAndFileName = storeClothingBinReportImages(report, images);
            uploadPathAndFileName.stream().forEach(filepath -> {
                reportImageRepository.save(ReportImage.of(reportMember, filepath));
            });
        }
    }

    private boolean isDuplicatedReport(ReportMemberId reportMemberId) {
        return reportMemberRepository.findById(reportMemberId).isPresent();
    }

    private void updateReportCountAndSaveClothingBinReport(Report report) {
        report.addReportCount();
        clothingBinReportRepository.save(report);
    }

    private List<String> storeClothingBinReportImages(Report report, List<MultipartFile> images) {
        final int MAX_IMAGE_FILE_COUNT = 3;
        if (images.size() > MAX_IMAGE_FILE_COUNT) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드 할 수 있습니다.");
        }

        final String DIRECTORY = "report/" + report.getReportId().toString();

        List<String> uploadPaths = new ArrayList<>();

        for (MultipartFile image : images) {
            if (Objects.isNull(image)) continue;
            String uploadPath = objectStorageService.uploadFileToObjectStorage(DIRECTORY, image);
            uploadPaths.add(uploadPath);
        }
        return uploadPaths;
    }
}
