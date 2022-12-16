package com.droptheclothes.api.service;

import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.repository.MemberRepository;
import com.droptheclothes.api.utility.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getActiveMemberById(String memberId) {
        return memberRepository.findByMemberIdAndIsRemoved(memberId, false)
                               .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHED_MEMBER_MESSAGE));
    }

    public Member getMemberByNickName(String nickName) {
        return memberRepository.findByNickName(nickName)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHED_MEMBER_MESSAGE));
    }
}
