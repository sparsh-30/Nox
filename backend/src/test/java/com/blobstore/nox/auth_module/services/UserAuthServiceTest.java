package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.dtos.LoginRequest;
import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.models.User;
import com.blobstore.nox.auth_module.utils.AuthCacheProvider;
import com.blobstore.nox.auth_module.utils.AuthEntitiesMapper;
import com.blobstore.nox.auth_module.utils.TokenType;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private UserAuthHelperService authHelperService;
    @Mock
    private AuthEntitiesMapper mapper;
    @Mock
    private JWTService jwtService;
    @Mock
    private AuthCacheProvider authCacheProvider;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserAuthService userAuthService;

    @Test
    void registerUser_throwsWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest("alice", "pass");
        when(authHelperService.doesUserExistByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> userAuthService.registerUser(response, request))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.AUTH_USER_ALREADY_EXISTS);
    }

    @Test
    void loginUser_throwsWhenUserNotFound() {
        LoginRequest request = new LoginRequest("alice", "pass");
        when(authHelperService.getUserByUsername("alice")).thenReturn(null);

        assertThatThrownBy(() -> userAuthService.loginUser(response, request))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.AUTH_USER_NOT_FOUND);
    }

    @Test
    void loginUser_throwsWhenPasswordMismatch() {
        LoginRequest request = new LoginRequest("alice", "wrong");
        User user = User.builder().userId("u1").username("alice").password("hash").build();
        when(authHelperService.getUserByUsername("alice")).thenReturn(user);
        when(authHelperService.doPasswordsMatch("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> userAuthService.loginUser(response, request))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.AUTH_INVALID_CREDENTIALS);
    }

    @Test
    void loginUser_succeedsAndStoresRefreshToken() {
        LoginRequest request = new LoginRequest("alice", "pass");
        User user = User.builder().userId("u1").username("alice").password("hash").build();
        when(authHelperService.getUserByUsername("alice")).thenReturn(user);
        when(authHelperService.doPasswordsMatch("pass", "hash")).thenReturn(true);
        when(jwtService.generateAccessToken("u1", "alice", TokenType.ACCESS)).thenReturn("access");
        when(jwtService.generateRefreshToken("u1", "alice", TokenType.REFRESH)).thenReturn("refresh");
        when(jwtService.extractUserIdAndTokenIdFromToken("refresh"))
                .thenReturn(Map.of("userId", "u1", "tokenId", "tid-1"));

        userAuthService.loginUser(response, request);

        verify(authCacheProvider).storeRefreshToken("u1", "tid-1", "refresh");
        verify(jwtService).setRefreshTokenCookie(response, "refresh");
        verify(mapper).generateLoginResponse(user, "access");
    }
}
