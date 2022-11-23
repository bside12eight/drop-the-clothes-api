package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.myinfo.MyInfoResponse;
import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.repository.ArticleRepository;
import com.droptheclothes.api.repository.ReportMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyInfoService {

    private final MemberService memberService;

    private final ReportMemberRepository reportMemberRepository;

    private final ArticleRepository articleRepository;

    public MyInfoResponse getMyInfo() {
        final String MEMBER_ID = "kakao_2467164020";
        return MyInfoResponse.of(memberService.getMemberById(MEMBER_ID));
    }

    @Transactional
    public void updateNickname(String nickname) {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);
        member.changeNickName(nickname);
    }

    @Transactional
    public void updatePassword(String currentPassword, String password) {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);
        member.changePassword(currentPassword, password);
    }

    public List<ClothingBinReportResponse> getMyClothingBinReports() {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);

        List<ReportMember> myReports = reportMemberRepository.findByMemberOOrderByCreatedAtDesc(member);
        return ClothingBinReportResponse.of(myReports);
    }

    public List<KeepOrDropArticleResponse> getMyKeepOrDropArticles() {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);

        List<Article> myArticles = articleRepository.findByMemberOrderByCreatedAtDesc(member);
        return KeepOrDropArticleResponse.of(myArticles);
    }
}
