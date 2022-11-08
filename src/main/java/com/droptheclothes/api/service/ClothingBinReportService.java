package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportRequest;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.Report;
import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.entity.pk.ReportMemberId;
import com.droptheclothes.api.model.enums.ReportType;
import com.droptheclothes.api.repository.ClothingBinReportRepository;
import com.droptheclothes.api.repository.ReportMemberRepository;
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

    private final ClothingBinReportRepository clothingBinReportRepository;

    private final ReportMemberRepository reportMemberRepository;

    @Transactional
    public void reportNewClothingBin(ClothingBinReportRequest request, MultipartFile image) {
        // TODO: Member ID 처리
        final String MEMBER_ID = "kakao_2467164020";

        if (clothingBinService.isRegisteredClothingBin(request.getAddress())) {
            throw new IllegalArgumentException("해당 위치에는 이미 등록된 의류수거함이 존재합니다.");
        }

        Member member = memberService.getMemberById(MEMBER_ID);

        Report report = clothingBinReportRepository.findByAddressAndType(request.getAddress(), ReportType.NEW)
                                        .orElse(Report.of(request));

        if (isDuplicatedReport(new ReportMemberId(report.getReportId(), member.getMemberId()))) {
            throw new IllegalArgumentException("이미 요청한 이력이 존재합니다.");
        }

        updateClothingBinReport(report);

        String uploadPathAndFileName = storeClothingBinReportImage(report, image);

        reportMemberRepository.save(ReportMember.of(report, member, uploadPathAndFileName));
    }

    private boolean isDuplicatedReport(ReportMemberId reportMemberId) {
        return reportMemberRepository.findById(reportMemberId).isPresent();
    }

    private void updateClothingBinReport(Report report) {
        report.addReportCount();
        clothingBinReportRepository.save(report);
    }

    private String storeClothingBinReportImage(Report report, MultipartFile image) {
        final String DIRECTORY = "report/" + report.getReportId().toString();

        if (Objects.isNull(image)) return null;
        return objectStorageService.uploadFileToObjectStorage(DIRECTORY, image);
    }
}
