package com.droptheclothes.api.service;

import com.droptheclothes.api.client.ClientKakao;
import com.droptheclothes.api.jwt.AuthToken;
import com.droptheclothes.api.jwt.AuthTokenProvider;
import com.droptheclothes.api.model.dto.AuthRequest;
import com.droptheclothes.api.model.dto.AuthResponse;
import com.droptheclothes.api.model.entity.Members;
import com.droptheclothes.api.repository.MemberQuerydslRepository;
import com.droptheclothes.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final ClientKakao clientKakao;
    private final MemberQuerydslRepository memberQuerydslRepository;
    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        Members kakaoMember = clientKakao.getUserData(authRequest.getAccessToken());
        String socialId = kakaoMember.getSocialId();
        Members member = memberQuerydslRepository.findBySocialId(socialId);

        AuthToken appToken = authTokenProvider.createUserAppToken(socialId);

        if (member == null) {
            memberRepository.save(kakaoMember);
            return AuthResponse.builder()
                    .appToken(appToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        }

        return AuthResponse.builder()
                .appToken(appToken.getToken())
                .isNewMember(Boolean.FALSE)
                .build();
    }
}
