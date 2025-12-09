package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.utils.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationTime;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, Object> generateClaimsMap(String userId, String username, TokenType tokenType) {
        return Map.of(
                "userId", userId,
                "username", username,
                "tokenType", tokenType
        );
    }

    public String generateAccessToken(String userId, String username, TokenType tokenType) {
        return Jwts.builder()
                .claims(generateClaimsMap(userId, username, tokenType))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String userId, String username, TokenType tokenType) {
        return Jwts.builder()
                .claims(generateClaimsMap(userId, username, tokenType))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        Claims claims;
        return Jwts.parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isAccessTokenValid(String token) {
        Claims tokenClaims = extractAllClaims(token);
        String tokenTypeStr = tokenClaims.get("tokenType", String.class);
        TokenType tokenType = TokenType.valueOf(tokenTypeStr);
        return tokenType == TokenType.ACCESS;
    }

    public Boolean isRefreshTokenValid(String token) {
        Claims tokenClaims = extractAllClaims(token);
        String tokenTypeStr = tokenClaims.get("tokenType", String.class);
        TokenType tokenType = TokenType.valueOf(tokenTypeStr);
        return tokenType == TokenType.REFRESH;
    }


}
