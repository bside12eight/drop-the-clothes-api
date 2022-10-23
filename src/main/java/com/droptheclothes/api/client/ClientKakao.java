package com.droptheclothes.api.client;

import com.droptheclothes.api.exception.TokenValidFailedException;
import com.droptheclothes.api.model.dto.KakaoUserResponse;
import com.droptheclothes.api.model.entity.Members;
import com.droptheclothes.api.model.enums.MemberProvider;
import com.droptheclothes.api.model.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClientKakao implements ClientProxy {

    private final WebClient webClient;

    // TODO ADMIN 유저 생성 시 getAdminUserData 메소드 생성 필요
    @Override
    public Members getUserData(String accessToken) {
        KakaoUserResponse kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return Members.builder()
                .socialId(String.valueOf(kakaoUserResponse.getId()))
                .name(kakaoUserResponse.getProperties().getNickname())
                .email(kakaoUserResponse.getKakaoAccount().getEmail())
                .gender(kakaoUserResponse.getKakaoAccount().getGender())
                .memberProvider(MemberProvider.KAKAO)
                .roleType(RoleType.USER)
                .profileImagePath(kakaoUserResponse.getProperties().getProfileImage() != null ? kakaoUserResponse.getProperties().getProfileImage() : "")
                .build();
    }
}
