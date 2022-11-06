package com.droptheclothes.api.service;

import com.droptheclothes.api.jwt.JwtTokenProvider;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.dto.auth.KakaoUserInfo;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.Oauth2UserInfo;
import com.droptheclothes.api.model.dto.auth.OauthTokenResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.repository.MemberRepository;
import com.droptheclothes.api.repository.OauthRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {

  private static final String BEARER_TYPE = "Bearer ";
  private final OauthRepository oauthRepository;
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;


  /**
   * 프론트에서 받은 AccessToken 정보를 DB에 저장하는 메소드
   * @param requestDto
   */
  public void saveAccessToken(OauthInfoRequest requestDto) {
    oauthRepository.save(requestDto.toEntity());
  }

  /**
   * 소셜 서버로부터 사용자 정보를 받아와서 로그인 처리를 진행함
   * @param providerName
   * @param
   * @return
   */
  public LoginResponse loginWithToken(String providerName, String Token){

    //1. 소셜 서버에 전달할 accessToken
    OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                                                          .accessToken(Token)
                                                          .tokenType(BEARER_TYPE)
                                                          .build();

    // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
    Member member = getUserProfile(providerName, tokenResponse);

    // 3. 앱에 전달할 jwt 토큰 발행하기
    String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getNickName()));
    String refreshToken = jwtTokenProvider.createRefreshToken();

    return LoginResponse.builder()
        .nickName(member.getNickName())
        .email(member.getEmail())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

  }

  public LoginResponse loginWithToken2(String providerName, String Token, String type){

    //1. 소셜 서버에 전달할 accessToken
    OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
        .accessToken(Token)
        .tokenType(BEARER_TYPE)
        .build();

    // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
    Member member = getUserProfile(providerName, tokenResponse);

    //이미 존재하는 회원인지 검증하는 과정
    Member memberEntity = memberRepository.findByMemberId(member.getMemberId());

    if(memberEntity == null) {
      type = "join";
    }
    else{
      type = "sign-in";
    }

    return LoginResponse.builder()
        .nickName(member.getNickName())
        .email(member.getEmail())
        .type(type)
        .build();

  }

  /**
   * KAKAO 소셜 로그인 서버에 Access Token을 통해 사용자 정보를 받아옴
   */
  private Member getUserProfile(
      String providerName
      , OauthTokenResponse tokenResponse
  ){
    Map<String, Object> userAttributes = getUserAttributes(providerName, tokenResponse);
    Oauth2UserInfo oauth2UserInfo = null;

    if(providerName.equals("kakao")){
      oauth2UserInfo = new KakaoUserInfo(userAttributes);
      log.info("카카오 고객 정보를 받아오는데 성공하였습니다");
    } else {
      log.info("허용되지 않은 AUTH 접근입니다");
    }

    String provider = oauth2UserInfo.getProvider();
    String providerId = providerName + "_" + oauth2UserInfo.getProviderId();
    String nickName = oauth2UserInfo.getNickName();
    String email = oauth2UserInfo.getEmail();

    //이미 존재하는 회원인지 검증하는 과정
    Member memberEntity = memberRepository.findByMemberId(providerId);

    if(memberEntity == null){
      memberEntity = Member.createMember(providerId, provider, nickName, email);
    }
    return memberEntity;
  }

  /**
   * 소셜 서버에 접속하여 사용자 정보를 받아오는 메소드
   */
  private Map<String, Object> getUserAttributes(String providerName, OauthTokenResponse tokenResponse){
    String uri = "";
    if(providerName.equals("kakao")) {
      uri = "https://kapi.kakao.com/v2/user/me";
    }

    return WebClient.create()
        .post()
        .uri(uri)
        .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();
  }

  public TokenResponse refreshToken(String nickName, String refreshToken) {
    boolean isValidate = jwtTokenProvider.validateToken(refreshToken);

    String newAccessToken = "";
    String newRefreshToken = "";

    if(!isValidate){
      newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(nickName));
      newRefreshToken = jwtTokenProvider.createRefreshToken();
      log.info("토큰을 재발급하였습니다");
    } else {
      log.info("토큰 재발급에 실패하였습니다");
    }

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build();

  }

  public Boolean checkNickName(String nickName) {
    Boolean checkNickName = false;
    Member memberEntitiy = memberRepository.findByNickName(nickName);

    if(memberEntitiy == null) {
      checkNickName = true;
    }
    return checkNickName;
  }

  public Boolean deleteProfile(String memberId) {
    Boolean isDelete = false;
    Member memberEntitiy = memberRepository.findByMemberId(memberId);

    if(memberEntitiy != null) {
      isDelete = true;
      memberRepository.delete(memberEntitiy);
    }
    return isDelete;
  }
}
