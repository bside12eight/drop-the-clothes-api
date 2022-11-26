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
import com.droptheclothes.api.security.SecurityUtility;
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
        return MyInfoResponse.of(memberService.getMemberById(SecurityUtility.getMemberId()));
    }

    @Transactional
    public void updateNickname(String nickname) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        member.changeNickName(nickname);
    }

    @Transactional
    public void updatePassword(String currentPassword, String password) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        member.changePassword(currentPassword, password);
    }

    public List<ClothingBinReportResponse> getMyClothingBinReports() {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        List<ReportMember> myReports = reportMemberRepository.findByMemberOrderByCreatedAtDesc(member);
        return ClothingBinReportResponse.of(myReports);
    }

    public List<KeepOrDropArticleResponse> getMyKeepOrDropArticles() {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        List<Article> myArticles = articleRepository.findByMemberOrderByCreatedAtDesc(member);
        return KeepOrDropArticleResponse.of(myArticles);
    }

    @Transactional
    public void blockUser(String nickName) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        Member blockedMember = memberService.getMemberByNickName(nickName);

        BlockedMember blockedMemberEntity = new BlockedMember(member, blockedMember, LocalDateTime.now());
        blockedMemberRepository.save(blockedMemberEntity);
    }

    public List<BlockedUserResponse> getBlockedUsers() {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        List<BlockedMember> blockedMembers = blockedMemberRepository.findByMemberOrderByCreatedAtDesc(member);
        return BlockedUserResponse.of(blockedMembers);
    }

    @Transactional
    public void unblockUser(String memberId) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        Member blockedMember = memberService.getMemberById(memberId);

        BlockedMember blockedMemberEntity = blockedMemberRepository.findById(new BlockedMemberId(member.getMemberId(), blockedMember.getMemberId()))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 차단 내역이 존재하지 않습니다."));

        blockedMemberRepository.delete(blockedMemberEntity);
    }
}
