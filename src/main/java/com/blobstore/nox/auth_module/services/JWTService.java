package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.utils.TokenType;
import com.blobstore.nox.common.UUIDGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    public Map<String, Object> generateAccessTokenClaimsMap(String userId, String username, TokenType tokenType) {
        return Map.of(
                "userId", userId,
                "username", username,
                "tokenType", tokenType
        );
    }

    public Map<String, Object> generateRefreshTokenClaimsMap(String userId, String username, TokenType tokenType) {
        return Map.of(
                "userId", userId,
                "username", username,
                "tokenType", tokenType,
                "tokenId", UUIDGenerator.generateUUIDString()
        );
    }

    public String generateAccessToken(String userId, String username, TokenType tokenType) {
        return Jwts.builder()
                .claims(generateAccessTokenClaimsMap(userId, username, tokenType))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime*1000))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String userId, String username, TokenType tokenType) {
        return Jwts.builder()
                .claims(generateRefreshTokenClaimsMap(userId, username, tokenType))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime*1000))
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

    public Map<String, String> extractUserIdAndTokenIdFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return Map.of(
                "userId", claims.get("userId", String.class),
                "tokenId", claims.get("tokenId", String.class)
        );
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

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/v1/refresh")
                .maxAge(refreshTokenExpirationTime)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
