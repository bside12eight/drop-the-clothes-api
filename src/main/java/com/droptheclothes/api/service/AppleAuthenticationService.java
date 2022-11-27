package com.droptheclothes.api.service;

import com.droptheclothes.api.exception.AuthenticationException;
import com.droptheclothes.api.model.dto.apple.ApplePublicKeys;
import com.droptheclothes.api.model.dto.apple.ApplePublicKeys.ApplePublicKey;
import com.droptheclothes.api.model.dto.apple.IdentityTokenHeader;
import com.droptheclothes.api.model.dto.auth.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleAuthenticationService implements AuthenticationService {

    @Override
    public void login(LoginRequest request) {
        IdentityTokenHeader identityTokenHeader = request.getIdentityTokenHeader();

        ApplePublicKey matchedApplePublicKey = requestApplePublicKeys()
                .getMatchedApplePublicKey(identityTokenHeader)
                .orElseThrow(() -> new AuthenticationException("매칭되는 public key를 찾을 수 없습니다."));

        Claims claims = getIdentityTokenClaims(request.getAccessToken(), matchedApplePublicKey);
        System.out.println(claims);
        // 회원가입 여부 확인
        // 회원가입한 경우
            // 인증용 JWT 토큰 생성해서 Front에 반환
        // 회원가입하지 않은 경우
            // OauthResponse 객체 반환
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
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new AuthenticationException("토큰 파싱 도중 문제가 발생하였습니다.");
        }
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
    }

    @Override
    public void signUp() {

    }

    private ApplePublicKeys requestApplePublicKeys() {
        return WebClient.create()
                .get()
                .uri("https://appleid.apple.com/auth/keys")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApplePublicKeys>() {
                })
                .block();
    }
}
