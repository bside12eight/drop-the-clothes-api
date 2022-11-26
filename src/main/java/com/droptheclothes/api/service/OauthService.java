package com.droptheclothes.api.service;

import com.droptheclothes.api.jwt.JwtTokenProvider;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.dto.auth.KakaoUserInfo;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.Oauth2UserInfo;
import com.droptheclothes.api.model.dto.auth.OauthResponse;
import com.droptheclothes.api.model.dto.auth.OauthTokenResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.enums.Provider;
import com.droptheclothes.api.model.enums.SignType;
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

  public void saveAccessToken(OauthInfoRequest requestDto) {
    oauthRepository.save(requestDto.toEntity());
  }

  public LoginResponse loginWithToken(String providerName, String Token){

    //1. 소셜 서버에 전달할 accessToken
    OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                                                          .accessToken(Token)
                                                          .tokenType(BEARER_TYPE)
                                                          .build();

    // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
    Member member = getUserProfile(providerName, tokenResponse);
    memberRepository.save(member); // 회원가입

    // 3. 앱에 전달할 jwt 토큰 발행하기
    String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
    String refreshToken = jwtTokenProvider.createRefreshToken();
    log.debug(String.format("accessToken: %s", accessToken));
    log.debug(String.format("refreshToken: %s", refreshToken));

    return LoginResponse.builder()
        .memberId(member.getMemberId())
        .nickName(member.getNickName())
        .email(member.getEmail())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .type(SignType.SIGNIN.getType())
        .build();

  }

  public LoginResponse joinWithToken(String providerName, String Token, String nickName){

    //1. 소셜 서버에 전달할 accessToken
    OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
        .accessToken(Token)
        .tokenType(BEARER_TYPE)
        .build();

    // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
    Member member = null;
    String type = "";
    if(
        ( nickName.isEmpty() )
            || (nickName.equals("") )
            || (nickName == null)
    ){
      type = SignType.SIGNIN.getType();
      member = getUserProfile(providerName, tokenResponse);
    }
    else{
      type = SignType.SIGNUP.getType();
      member = getUserProfileWithNewNickName(providerName, tokenResponse, nickName);
    }

    memberRepository.save(member); // 회원가입

    // 3. 앱에 전달할 jwt 토큰 발행하기
    String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getNickName()));
    String refreshToken = jwtTokenProvider.createRefreshToken();
    log.debug(String.format("accessToken: %s", accessToken));
    log.debug(String.format("refreshToken: %s", refreshToken));

    return LoginResponse.builder()
        .nickName(member.getNickName())
        .email(member.getEmail())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .type(type)
        .build();

  }


  public OauthResponse checkExistMemberWithToken(String providerName, String Token){

    String type = "";

    //1. 소셜 서버에 전달할 accessToken
    OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
        .accessToken(Token)
        .tokenType(BEARER_TYPE)
        .build();

    // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
    Member member = getUserProfile(providerName, tokenResponse);

    //이미 존재하는 회원인지 검증하는 과정
    Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(member.getMemberId(), false);

    if(memberEntity == null) {
      type = SignType.SIGNUP.getType();
    }
    else{
      type = SignType.SIGNIN.getType();
    }

    return OauthResponse.builder()
        .accessToken(Token)
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


    if(providerName.equals(Provider.kakao.name())){
      oauth2UserInfo = new KakaoUserInfo(userAttributes);
      log.info("카카오 고객 정보를 받아오는데 성공하였습니다");
    }
    else if(providerName.equals(Provider.apple.name())){
      //oauth2UserInfo = new AppleUserInfo(userAttributes);
    }
    else {
      log.info("허용되지 않은 AUTH 접근입니다");
    }

    String provider = oauth2UserInfo.getProvider();
    String providerId = providerName + "_" + oauth2UserInfo.getProviderId();
    String nickName = oauth2UserInfo.getNickName();
    String email = oauth2UserInfo.getEmail();

    //이미 존재하는 회원인지 검증하는 과정
    Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(providerId, false);

    if(memberEntity == null){
      memberEntity = Member.createMember(providerId, provider, nickName, email);
    }

    return memberEntity;
  }

  private Member getUserProfileWithNewNickName(
      String providerName
      , OauthTokenResponse tokenResponse
      , String nickName
  ){
    Map<String, Object> userAttributes = getUserAttributes(providerName, tokenResponse);
    Oauth2UserInfo oauth2UserInfo = null;

    if(providerName.equals(Provider.kakao.name())){
      oauth2UserInfo = new KakaoUserInfo(userAttributes);
      log.info("카카오 고객 정보를 받아오는데 성공하였습니다");
    } else {
      log.info("허용되지 않은 AUTH 접근입니다");
    }

    String provider = oauth2UserInfo.getProvider();
    String providerId = providerName + "_" + oauth2UserInfo.getProviderId();
    String email = oauth2UserInfo.getEmail();

    //이미 존재하는 회원인지 검증하는 과정
    Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(providerId, false);

    if(memberEntity == null){
      memberEntity = Member.createMember(providerId, provider, nickName, email);
    }
    else{
      memberEntity.changeNickName(nickName);
    }

    return memberEntity;
  }

  /**
   * 소셜 서버에 접속하여 사용자 정보를 받아오는 메소드
   */
  private Map<String, Object> getUserAttributes(String providerName, OauthTokenResponse tokenResponse){
    String uri = "";
    if(providerName.equals(Provider.kakao.name())) {
      uri = "https://kapi.kakao.com/v2/user/me";
    }
    else if(providerName.equals(Provider.apple.name())){
      ;
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

  public Boolean isExistNickName(String nickName) {
    Boolean isExistNickName = false;
    Member memberEntitiy = memberRepository.findByNickName(nickName);

    if(memberEntitiy != null) {
      isExistNickName = true;
      //throw new DropTheClothesApiException(ResponseCode.EXIST_NICKNAME);
    }
    return isExistNickName;
  }

  public Boolean updateNickName(String memberId, String nickName) {
    Boolean changeNickName = false;
    Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(memberId, false);

    if( !isExistNickName(nickName) ){
      memberEntity.changeNickName(nickName);
      memberRepository.save(memberEntity);
      changeNickName = true;
    }
    else{
      throw new IllegalArgumentException("존재하는 회원이 없습니다.");
    }

    return changeNickName;
  }


  public Boolean deleteProfile(String memberId) {
    Boolean isDelete = false;
    Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(memberId,false);

    if(memberEntity != null) {
      isDelete = true;
      memberEntity.removeMember();
      memberRepository.save(memberEntity);
    }
    else{
      throw new IllegalArgumentException("존재하는 회원이 없습니다.");
    }

    return isDelete;
  }

  public OauthResponse getProfileById(String memberId) {
    Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(memberId, false);

    if(memberEntity == null){
      throw new IllegalArgumentException("존재하는 회원이 없습니다.");
    }

    return OauthResponse.builder()
        .nickName(memberEntity.getNickName())
        .email(memberEntity.getEmail())
        .type(memberEntity.getMemberId().split("_")[0])
        .build();
  }
}
