package com.droptheclothes.api.service;

import com.droptheclothes.api.jwt.JwtTokenProvider;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.dto.auth.KakaoUserInfo;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.Oauth2UserInfo;
import com.droptheclothes.api.model.dto.auth.OauthTokenResponse;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.repository.MemberRepository;
import com.droptheclothes.api.repository.OauthRepository;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {

  private static final String BEARER_TYPE = "Bearer ";
  private final OauthRepository oauthRepository;
  private final MemberRepository memberRepository;
  private final InMemoryClientRegistrationRepository inMemoryRepository;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 프론트에서 받은 AccessToken 정보를 DB에 저장함
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

    /**
     * 소셜 서버에서 사용자 정보 받아오기
     */
    ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName); // 소셜 provider 확인하기

    OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                                                          .accessToken(Token)
                                                          .tokenType(BEARER_TYPE)
                                                          .build();

    Member member = getUserProfile(providerName, tokenResponse, provider); // 사용자 정보 얻기

    /**
     * 앱에 전달할 jwt 토큰 발행하기
     */
    String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
    String refreshToken = jwtTokenProvider.createRefreshToken();

    return LoginResponse.builder()
        .provider(providerName)
        .memberId(member.getMemberId())
        .nickName(member.getNickName())
        .email(member.getEmail())
        .role(member.getRole())
        .tokenType(BEARER_TYPE)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

  }





  /**
   * 소셜 서버로부터 사용자 정보를 받아와서 로그인 처리를 진행함
   * @param providerName
   * @param code
   * @return
   */
  public LoginResponse login(String providerName, String code){

    /**
     * 소셜 서버에서 사용자 정보 받아오기
     */
    ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName); // 소셜 provider 확인하기
    OauthTokenResponse tokenResponse = getToken(code, provider); // 액세스 토큰 얻기
    log.info("액세스 토큰 얻기 성공" + tokenResponse.getAccessToken());
    Member member = getUserProfile(providerName, tokenResponse, provider); // 사용자 정보 얻기

    /**
     * 앱에 전달할 jwt 토큰 발행하기
     */
    String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
    String refreshToken = jwtTokenProvider.createRefreshToken();

    return LoginResponse.builder()
        .provider(providerName)
        .memberId(member.getMemberId())
        .nickName(member.getNickName())
        .email(member.getEmail())
        .role(member.getRole())
        .tokenType(BEARER_TYPE)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

  }





  private OauthTokenResponse getToken(String code, ClientRegistration provider){
        return WebClient.create()
            .post()
            .uri(provider.getProviderDetails().getTokenUri())
            .headers(header -> {
              header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
              header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            })
            .bodyValue(tokenRequest(code, provider))
            .retrieve()
            .bodyToMono(OauthTokenResponse.class)
            .block();
  }

  /**
   * KAKAO 소셜 로그인 서버에 접근할 Access Token을 받아오는 메소드
   * @param code
   * @param provider
   * @return
   */
  private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider){
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("code", code);
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", provider.getClientId());
    formData.add("redirect_uri", provider.getRedirectUri());
    formData.add("client_secret", provider.getClientSecret());

    return formData;
  }

  /**
   * KAKAO 소셜 로그인 서버에 Access Token을 통해 사용자 정보를 받아옴
   * @param providerName
   * @param tokenResponse
   * @param provider
   * @return
   */
  private Member getUserProfile(
      String providerName
      , OauthTokenResponse tokenResponse
      , ClientRegistration provider
  ){
    Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
    Oauth2UserInfo oauth2UserInfo = null;

    if(providerName.equals("kakao")){
      oauth2UserInfo = new KakaoUserInfo(userAttributes);
      log.info("카카오 고객 정보를 받아오는데 성공하였습니다" + oauth2UserInfo.getNickName());
    } else {
      log.info("허용되지 않은 AUTH 접근입니다");
    }

    String provide = oauth2UserInfo.getProvider();
    String providerId = oauth2UserInfo.getProviderId();
    String nickName = oauth2UserInfo.getNickName();
    String email = oauth2UserInfo.getEmail();

    /**
     * 이미 존재하는 회원인지 검증하는 과정
     */
    Member memberEntitiy = memberRepository.findByEmail(email);

    if(memberEntitiy == null){
      memberEntitiy = Member.createMember(email,nickName, provide, providerId);
      memberRepository.save(memberEntitiy);
    }

    return memberEntitiy;

  }


  private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse){
    return WebClient.create()
        .post()
        .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
        .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();
  }

}
