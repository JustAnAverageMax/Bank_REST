package com.example.bankcards.service.impl;

import com.example.bankcards.service.JwtService;
import com.example.bankcards.util.ConstantsUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access.duration}")
  private Duration accessTokenDuration;

  @Value("${jwt.refresh.duration}")
  private Duration refreshTokenDuration;

  private Key secretKey;

  @Override
  public String generateAccessToken(UserDetails userDetails) {
    return buildToken(userDetails, accessTokenDuration);
  }

  @Override
  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(userDetails, refreshTokenDuration);
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractEmail(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  @Override
  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private boolean isTokenExpired(String token) {
    ZonedDateTime now = ZonedDateTime.now(ConstantsUtil.DEFAULT_TIME_ZONE);
    return extractExpiration(token).before(Date.from(now.toInstant()));
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private String buildToken(UserDetails userDetails, Duration tokenDuration) {
    ZonedDateTime now = ZonedDateTime.now(ConstantsUtil.DEFAULT_TIME_ZONE);
    ZonedDateTime expiryDate = now.plus(tokenDuration);

    Map<String, Object> claims = new HashMap<>();
    if (tokenDuration.equals(accessTokenDuration)) {
      claims.put("role", userDetails.getAuthorities());
    }

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(expiryDate.toInstant()))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  @PostConstruct
  private void init() {
    secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

}
