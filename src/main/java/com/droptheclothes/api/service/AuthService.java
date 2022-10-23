package com.droptheclothes.api.service;

import com.droptheclothes.api.jwt.AuthToken;
import com.droptheclothes.api.jwt.AuthTokenProvider;
import com.droptheclothes.api.model.dto.AuthResponse;
import com.droptheclothes.api.model.entity.Members;
import com.droptheclothes.api.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;
    //private final MemberQuerydslRepository memberQuerydslRepository;

    public AuthResponse updateToken(AuthToken authToken) {
        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return null;
        }

        String socialId = claims.getSubject();

        AuthToken newAppToken = authTokenProvider.createUserAppToken(socialId);

        return AuthResponse.builder()
                .appToken(newAppToken.getToken())
                .build();
    }

    public Long getMemberId(String token) {
        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return null;
        }

        try {
            long id = 30;
            Members member =  memberRepository.findMemberById(id);
            //Members member =  memberQuerydslRepository.findBySocialId(claims.getSubject());
            return member.getId();

        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.");
        }
    }
}
