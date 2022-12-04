package com.droptheclothes.api.service;

import com.droptheclothes.api.exception.AppleRequestException;
import com.droptheclothes.api.jwt.JwtTokenProvider;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.dto.apple.AppleTokenResponse;
import com.droptheclothes.api.model.dto.auth.KakaoUserInfo;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.Oauth2UserInfo;
import com.droptheclothes.api.model.dto.auth.OauthResponse;
import com.droptheclothes.api.model.dto.auth.OauthTokenResponse;
import com.droptheclothes.api.model.dto.auth.TokenResponse;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.enums.LoginProviderType;
import com.droptheclothes.api.model.enums.SignType;
import com.droptheclothes.api.repository.MemberRepository;
import com.droptheclothes.api.repository.OauthRepository;
import com.droptheclothes.api.security.SecurityUtility;
import com.droptheclothes.api.utility.MessageConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {

    private static final String BEARER_TYPE = "Bearer ";
    private final OauthRepository oauthRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${apple.key-id}")
    private String appleSignKeyId;

    @Value("${apple.team-id}")
    private String appleTeamId;

    @Value("${apple.bundle-id}")
    private String appleBundleId;

    public void saveAccessToken(OauthInfoRequest requestDto) {
        oauthRepository.save(requestDto.toEntity());
    }

    public LoginResponse loginWithToken(LoginProviderType provider, String Token){

        //1. 소셜 서버에 전달할 accessToken
        OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                .accessToken(Token)
                .tokenType(BEARER_TYPE)
                .build();

        // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
        Member member = getUserProfile(provider, tokenResponse);
        memberRepository.save(member); // 회원가입

        // 3. 앱에 전달할 jwt 토큰 발행하기
        String accessToken = jwtTokenProvider.createAccessToken(
                String.valueOf(member.getMemberId()));
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

    public LoginResponse joinWithToken(LoginProviderType provider, String Token, String nickName){

        //1. 소셜 서버에 전달할 accessToken
        OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                .accessToken(Token)
                .tokenType(BEARER_TYPE)
                .build();

        // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
        Member member = null;
        String type = "";
        if (
                (nickName.isEmpty())
                        || (nickName.equals(""))
                        || (nickName == null)
        ) {
            type = SignType.SIGNIN.getType();
            member = getUserProfile(provider, tokenResponse);
        } else {
            type = SignType.SIGNUP.getType();
            member = getUserProfileWithNewNickName(provider, tokenResponse, nickName);
        }

        memberRepository.save(member); // 회원가입

        // 3. 앱에 전달할 jwt 토큰 발행하기
        String accessToken = jwtTokenProvider.createAccessToken(
                String.valueOf(member.getMemberId()));
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


    public OauthResponse checkExistMemberWithToken(LoginProviderType provider, String Token){

        String type = "";

        //1. 소셜 서버에 전달할 accessToken
        OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                .accessToken(Token)
                .tokenType(BEARER_TYPE)
                .build();

        // 2.accessToken을 사용해서 소셜 서버로부터 사용자 정보 얻기
        Member member = getUserProfile(provider, tokenResponse);

        //이미 존재하는 회원인지 검증하는 과정
        Member memberEntity = memberRepository.findByMemberIdAndIsRemoved(member.getMemberId(), false).orElse(null);

        if (memberEntity == null) {
            type = SignType.SIGNUP.getType();
        } else {
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
            LoginProviderType provider
            , OauthTokenResponse tokenResponse
    ) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        Oauth2UserInfo oauth2UserInfo = null;

        if (provider.equals(provider.kakao)) {
            oauth2UserInfo = new KakaoUserInfo(userAttributes);
            log.info("카카오 고객 정보를 받아오는데 성공하였습니다");
        } else if (provider.equals(LoginProviderType.apple)) {
            //oauth2UserInfo = new AppleUserInfo(userAttributes);
        } else {
            log.info("허용되지 않은 AUTH 접근입니다");
        }

        String providerId = provider + "_" + oauth2UserInfo.getProviderId();
        String nickName = oauth2UserInfo.getNickName();
        String email = oauth2UserInfo.getEmail();

        //이미 존재하는 회원인지 검증하는 과정
        Member member = memberRepository.findByMemberIdAndIsRemoved(providerId, false).orElse(null);

        if (member == null) {
            member = Member.createMember(providerId, provider, nickName, email);
        }

        return member;
    }

    private Member getUserProfileWithNewNickName(
            LoginProviderType provider
            , OauthTokenResponse tokenResponse
            , String nickName
    ) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        Oauth2UserInfo oauth2UserInfo = null;

        if (provider.equals(LoginProviderType.kakao)) {
            oauth2UserInfo = new KakaoUserInfo(userAttributes);
            log.info("카카오 고객 정보를 받아오는데 성공하였습니다");
        } else {
            log.info("허용되지 않은 AUTH 접근입니다");
        }

        String providerId = provider + "_" + oauth2UserInfo.getProviderId();
        String email = oauth2UserInfo.getEmail();

        //이미 존재하는 회원인지 검증하는 과정
        Member member = memberRepository.findByMemberIdAndIsRemoved(providerId, false).orElse(null);

        if (member == null) {
            member = Member.createMember(providerId, provider, nickName, email);
        } else {
            member.changeNickName(nickName);
        }

        return member;
    }

    /**
     * 소셜 서버에 접속하여 사용자 정보를 받아오는 메소드
     */
    private Map<String, Object> getUserAttributes(LoginProviderType provider,
            OauthTokenResponse tokenResponse) {
        String uri = "";
        if (provider.equals(LoginProviderType.kakao)) {
            uri = "https://kapi.kakao.com/v2/user/me";
        } else if (provider.equals(LoginProviderType.apple)) {
            ;
        }

        return WebClient.create()
                .post()
                .uri(uri)
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    public TokenResponse refreshToken(String nickName, String refreshToken) {
        boolean isValidate = jwtTokenProvider.validateToken(refreshToken);

        String newAccessToken = "";
        String newRefreshToken = "";

        if (!isValidate) {
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

    public Boolean isNickNameExist(String nickName) {
        return memberRepository.findByNickName(nickName).isPresent();
    }


    @Transactional
    public boolean deleteMember(String authorizationCode) {
        Member member = memberRepository.findByMemberIdAndIsRemoved(SecurityUtility.getMemberId(), false)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHED_MEMBER_MESSAGE));

        member.removeMember();
        memberRepository.save(member);

        if (member.getProvider().equals(LoginProviderType.apple)) {
            if (Objects.isNull(authorizationCode)) {
                throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
            }

            this.revoke(authorizationCode);
        }
        return true;
    }

    private void revoke(String authorizationCode) {
        AppleTokenResponse appleTokenResponse = GenerateAuthToken(authorizationCode);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleBundleId);
        params.add("client_secret", createClientSecret());
        params.add("token", appleTokenResponse.getAccessToken());

        if (appleTokenResponse.getAccessToken() != null) {
            WebClient.create()
                    .post()
                    .uri("https://appleid.apple.com/auth/revoke")
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {})
                    .block();
        }
    }

    private AppleTokenResponse GenerateAuthToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleBundleId);
        params.add("client_secret", createClientSecret());
        params.add("grant_type", "authorization_code");
        params.add("code", authorizationCode);

        WebClient.create()
                .post()
                .uri("https://appleid.apple.com/auth/token")
                .bodyValue(params)
                .exchange()
                .flatMap(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.error(new AppleRequestException(MessageConstants.APPLE_TOKEN_ERROR_MESSAGE));
                    }
                    return clientResponse.bodyToMono(AppleTokenResponse.class);
                })
                .block();

        return null;
    }

    private String createClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "ES256");
        header.put("kid", appleSignKeyId);

        return Jwts.builder()
                .setHeaderParams(header)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                .setExpiration(expirationDate) // 만료 시간
                .setAudience("https://appleid.apple.com")
                .setSubject(appleBundleId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            ClassPathResource resource = new ClassPathResource("auth-key.p8");
            String content = new String(Files.readAllBytes(Paths.get(resource.getURI())), "utf-8");
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("EC");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Java did not support the algorithm:" + "EC", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid key format");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
