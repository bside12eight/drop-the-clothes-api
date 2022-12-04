package com.droptheclothes.api.service;

import com.droptheclothes.api.exception.AuthenticationException;
import com.droptheclothes.api.jwt.JwtTokenProvider;
import com.droptheclothes.api.model.dto.apple.ApplePublicKeys;
import com.droptheclothes.api.model.dto.apple.ApplePublicKeys.ApplePublicKey;
import com.droptheclothes.api.model.dto.apple.IdentityTokenHeader;
import com.droptheclothes.api.model.dto.auth.JoinRequest;
import com.droptheclothes.api.model.dto.auth.LoginRequest;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.dto.auth.OauthResponse;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.enums.LoginProviderType;
import com.droptheclothes.api.model.enums.Role;
import com.droptheclothes.api.model.enums.SignType;
import com.droptheclothes.api.repository.MemberRepository;
import com.droptheclothes.api.utility.MessageConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleAuthenticationService implements AuthenticationService {

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${apple.public-key.endpoint}")
    private String applePublicKeyEndpoint;

    @Override
    public Object login(LoginRequest request) {
        IdentityTokenHeader identityTokenHeader = request.getIdentityTokenHeader();

        ApplePublicKey matchedApplePublicKey = getMatchedApplePublicKey(identityTokenHeader);

        Claims claims = getIdentityTokenClaims(request.getIdentityToken(), matchedApplePublicKey);

        Member member = memberRepository.findByMemberId(String.format("apple_%s", claims.get("email")))
                .orElse(null);

        // 회원가입 여부 확인
        if (Objects.isNull(member)) {
            return OauthResponse.builder()
                    .identityToken(request.getIdentityToken())
                    .type(SignType.SIGNUP.getType())
                    .build();
        } else {
            String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
            String refreshToken = jwtTokenProvider.createRefreshToken();

            return LoginResponse.builder()
                    .memberId(member.getMemberId())
                    .nickName(member.getNickName())
                    .email(member.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .type(SignType.SIGNIN.getType())
                    .build();
        }
    }

    @Override
    public LoginResponse signUp(LoginProviderType provider, JoinRequest request) {
        IdentityTokenHeader identityTokenHeader = request.getIdentityTokenHeader();

        ApplePublicKey matchedApplePublicKey = getMatchedApplePublicKey(identityTokenHeader);

        Claims claims = getIdentityTokenClaims(request.getIdentityToken(), matchedApplePublicKey);

        // Member 저장
        Member member = Member.builder()
                .memberId(provider + "_" + claims.get("email"))
                .provider(provider)
                .role(Role.USER)
                .nickName(request.getNickName())
                .email(claims.get("email").toString())
                .build();
        memberRepository.save(member);

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getMemberId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return LoginResponse.builder()
                .memberId(member.getMemberId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type(SignType.SIGNIN.getType())
                .build();
    }

    private ApplePublicKey getMatchedApplePublicKey(IdentityTokenHeader identityTokenHeader) {
        ApplePublicKey matchedApplePublicKey = requestApplePublicKeys()
                .getMatchedApplePublicKey(identityTokenHeader)
                .orElseThrow(() -> new AuthenticationException(MessageConstants.APPLE_PUBLIC_KEY_ERROR_MESSAGE));
        return matchedApplePublicKey;
    }

    private Claims getIdentityTokenClaims(String identityToken, ApplePublicKey applePublicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        PublicKey publicKey;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            publicKey = keyFactory.generatePublic(publicKeySpec);
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new AuthenticationException(MessageConstants.TOKEN_PARSING_ERROR_MESSAGE);
        }
    }

    private ApplePublicKeys requestApplePublicKeys() {
        return WebClient.create()
                .get()
                .uri(applePublicKeyEndpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApplePublicKeys>() {
                })
                .block();
    }
}
