package com.droptheclothes.api.service;

import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId)
                               .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
