package com.droptheclothes.api.jwt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  @Value("${jwt.access-token.expire-length}")
  private long accessTokenValidityInMilliseconds;

  @Value("${jwt.refresh-token.expire-length}")
  private long refreshTokenValidityInMilliseconds;

  @Value("${jwt.token.secret-key}")
  private String secretKey;

  public String createAccessToken(String payload) {
    return createToken(payload, accessTokenValidityInMilliseconds);
  }

  public String createRefreshToken() {
    byte[] array = new byte[7];
    new Random().nextBytes(array);
    String generatedString = new String(array, StandardCharsets.UTF_8);
    return createToken(generatedString, refreshTokenValidityInMilliseconds);
  }

  public String createToken(String payload, long expireLength) {
    Claims claims = Jwts.claims().setSubject(payload);
    Date now = new Date();
    Date validity = new Date(now.getTime() + expireLength);
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256,secretKey)
        .compact();
  }

  public String getPayload(String token){
    if (StringUtils.isBlank(token)) {
      return null;
    }

    token = token.replace("Bearer ", "");
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (ExpiredJwtException e) {
      return e.getClaims().getSubject();
    } catch (JwtException e){
      log.warn("유효하지 않은 토큰 입니다");
      return null;
    }
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException exception) {
      return false;
    }
  }
}