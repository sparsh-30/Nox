package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.utils.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JWTServiceTest {

    private static final String SECRET = "test-jwt-secret-key-must-be-at-least-32-chars-long";

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpirationTime", 300L);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpirationTime", 3600L);
    }

    @Test
    void generateAccessToken_isValidAccessToken() {
        String token = jwtService.generateAccessToken("user-1", "alice", TokenType.ACCESS);

        assertThat(jwtService.isAccessTokenValid(token)).isTrue();
        assertThat(jwtService.isRefreshTokenValid(token)).isFalse();
    }

    @Test
    void generateRefreshToken_isValidRefreshTokenAndContainsIds() {
        String token = jwtService.generateRefreshToken("user-1", "alice", TokenType.REFRESH);

        assertThat(jwtService.isRefreshTokenValid(token)).isTrue();
        Map<String, String> ids = jwtService.extractUserIdAndTokenIdFromToken(token);
        assertThat(ids.get("userId")).isEqualTo("user-1");
        assertThat(ids.get("tokenId")).isNotBlank();
    }

    @Test
    void extractRefreshToken_readsCookieWhenPresent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("other", "x"),
                new Cookie("refreshToken", "rt-value")
        });

        assertThat(jwtService.extractRefreshToken(request)).isEqualTo("rt-value");
    }

    @Test
    void extractRefreshToken_returnsNullWhenNoCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        assertThat(jwtService.extractRefreshToken(request)).isNull();
    }
}
