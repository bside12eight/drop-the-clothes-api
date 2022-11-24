package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinReportResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.myinfo.BlockedUserResponse;
import com.droptheclothes.api.model.dto.myinfo.MyInfoResponse;
import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.BlockedMember;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.ReportMember;
import com.droptheclothes.api.model.entity.pk.BlockedMemberId;
import com.droptheclothes.api.repository.ArticleRepository;
import com.droptheclothes.api.repository.BlockedMemberRepository;
import com.droptheclothes.api.repository.ReportMemberRepository;
import java.time.LocalDateTime;
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

    private final BlockedMemberRepository blockedMemberRepository;

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

        List<ReportMember> myReports = reportMemberRepository.findByMemberOrderByCreatedAtDesc(member);
        return ClothingBinReportResponse.of(myReports);
    }

    public List<KeepOrDropArticleResponse> getMyKeepOrDropArticles() {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);

        List<Article> myArticles = articleRepository.findByMemberOrderByCreatedAtDesc(member);
        return KeepOrDropArticleResponse.of(myArticles);
    }

    @Transactional
    public void blockUser(String nickName) {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);
        Member blockedMember = memberService.getMemberByNickName(nickName);

        BlockedMember blockedMemberEntity = new BlockedMember(member, blockedMember, LocalDateTime.now());
        blockedMemberRepository.save(blockedMemberEntity);
    }

    public List<BlockedUserResponse> getBlockedUsers() {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);

        List<BlockedMember> blockedMembers = blockedMemberRepository.findByMemberOrderByCreatedAtDesc(member);
        return BlockedUserResponse.of(blockedMembers);
    }

    @Transactional
    public void unblockUser(String memberId) {
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);
        Member blockedMember = memberService.getMemberById(memberId);

        BlockedMember blockedMemberEntity = blockedMemberRepository.findById(new BlockedMemberId(member.getMemberId(), blockedMember.getMemberId()))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 차단 내역이 존재하지 않습니다."));

        blockedMemberRepository.delete(blockedMemberEntity);
    }
}
