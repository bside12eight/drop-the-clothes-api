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
import com.droptheclothes.api.repository.MemberRepository;
import com.droptheclothes.api.repository.ReportMemberRepository;
import com.droptheclothes.api.security.SecurityUtility;
import com.droptheclothes.api.utility.BusinessConstants;
import com.droptheclothes.api.utility.MessageConstants;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MyInfoService {

    private final MemberService memberService;

    private final ObjectStorageService objectStorageService;

    private final ReportMemberRepository reportMemberRepository;

    private final ArticleRepository articleRepository;

    private final BlockedMemberRepository blockedMemberRepository;

    private final MemberRepository memberRepository;

    public MyInfoResponse getMyInfo() {
        Member member = memberRepository.findByMemberId(SecurityUtility.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHED_MEMBER_MESSAGE));
        if (member.isRemoved()) {
            throw new IllegalArgumentException(MessageConstants.REMOVED_MEMBER_MESSAGE);
        }
        return MyInfoResponse.of(member);
    }

    @Transactional
    public MyInfoResponse updateNickname(String nickname) {
        if (memberRepository.findByNickName(nickname).isPresent()) {
            throw new IllegalArgumentException(MessageConstants.ALREADY_EXIST_NICKNAME_MESSAGE);
        }
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        member.changeNickName(nickname);

        return MyInfoResponse.of(member);
    }

    @Transactional
    public void updatePassword(String currentPassword, String password) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        if (member.isSocialLoginMember()) {
            throw new IllegalArgumentException(MessageConstants.NO_NEED_PASSWORD_MESSAGE);
        }
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
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));

        blockedMemberRepository.delete(blockedMemberEntity);
    }

    @Transactional
    public MyInfoResponse updateProfileImage(MultipartFile image) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());
        final String DIRECTORY = BusinessConstants.PROFILE_IMAGE_DIRECTORY + member.getMemberId();

        String imageUrl = objectStorageService.uploadFileToObjectStorage(DIRECTORY, image);
        member.changeProfileImage(imageUrl);

        return MyInfoResponse.of(member);
    }
}
