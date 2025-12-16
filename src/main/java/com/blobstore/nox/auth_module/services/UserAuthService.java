package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.dtos.LoginRequest;
import com.blobstore.nox.auth_module.dtos.LoginResponse;
import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.dtos.RegisterResponse;
import com.blobstore.nox.auth_module.models.User;
import com.blobstore.nox.auth_module.utils.AuthCacheProvider;
import com.blobstore.nox.auth_module.utils.AuthEntitiesMapper;
import com.blobstore.nox.auth_module.utils.TokenType;
import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthHelperService authHelperService;
    private final AuthEntitiesMapper mapper;
    private final JWTService jwtService;
    private final AuthCacheProvider authCacheProvider;

    public RegisterResponse registerUser(HttpServletResponse response, RegisterRequest registerRequest) {
        if (authHelperService.doesUserExistByUsername(registerRequest.username())) {
            throw new CustomAppException("Username already exists", ResponseCode.AUTH_USER_ALREADY_EXISTS);
        }
        String hashedPassword = authHelperService.getHashedPassword(registerRequest.password());
        User u = mapper.mapRegisterRequestToUser(registerRequest, UUIDGenerator.generateUUIDString(), hashedPassword);
        User savedUser = authHelperService.saveUserEntity(u);
        String accessToken = jwtService.generateAccessToken(savedUser.getUserId(), savedUser.getUsername(), TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(savedUser.getUserId(), savedUser.getUsername(), TokenType.REFRESH);
        Map<String, String> refreshTokenKeys = jwtService.extractUserIdAndTokenIdFromToken(refreshToken);
        authCacheProvider.storeRefreshToken(refreshTokenKeys.get("userId"), refreshTokenKeys.get("tokenId"), refreshToken);
        jwtService.setRefreshTokenCookie(response, refreshToken);
        return mapper.generateRegisterResponse(savedUser, accessToken);
    }

    public LoginResponse loginUser(HttpServletResponse response, LoginRequest loginRequest) {
        User u = authHelperService.getUserByUsername(loginRequest.username());
        if (u == null) {
            throw new CustomAppException("User not found", ResponseCode.AUTH_USER_NOT_FOUND);
        }
        if (!authHelperService.doPasswordsMatch(loginRequest.password(), u.getPassword())) {
            throw new CustomAppException("Invalid credentials", ResponseCode.AUTH_INVALID_CREDENTIALS);
        }
        String accessToken = jwtService.generateAccessToken(u.getUserId(), u.getUsername(), TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(u.getUserId(), u.getUsername(), TokenType.REFRESH);
        Map<String, String> refreshTokenKeys = jwtService.extractUserIdAndTokenIdFromToken(refreshToken);
        authCacheProvider.storeRefreshToken(refreshTokenKeys.get("userId"), refreshTokenKeys.get("tokenId"), refreshToken);
        jwtService.setRefreshTokenCookie(response, refreshToken);
        return mapper.generateLoginResponse(u, accessToken);
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response, String userId) {
        User u = authHelperService.getUserByUserId(userId);
        if (u == null) {
            throw new CustomAppException("User not found", ResponseCode.AUTH_USER_NOT_FOUND);
        }
        String refreshToken = jwtService.extractRefreshToken(request);
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new CustomAppException("Refresh token is not valid", ResponseCode.INVALID_REFRESH_TOKEN);
        }
        Map<String, String> temp = jwtService.extractUserIdAndTokenIdFromToken(refreshToken);
        String tokenId = temp.get("tokenId");
        authCacheProvider.revokeRefreshToken(userId, tokenId);
        jwtService.revokeRefreshTokenCookie(response, refreshToken);
    }

    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        if (jwtService.extractRefreshToken(request) == null) {
            throw new CustomAppException("No refresh token is present", ResponseCode.REFRESH_TOKEN_NOT_PRESENT);
        }
        String refreshToken = jwtService.extractRefreshToken(request);
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new CustomAppException("Refresh token is not valid", ResponseCode.INVALID_REFRESH_TOKEN);
        }
        Map<String, String> refreshTokenKeys = jwtService.extractUserIdAndTokenIdFromToken(refreshToken);
        if (!authCacheProvider.isRefreshTokenPresent(refreshTokenKeys.get("userId"), refreshTokenKeys.get("tokenId"))) {
            throw new CustomAppException("User session is revoked, please try to login", ResponseCode.USER_SESSION_ALREADY_REVOKED);
        }
        User u = authHelperService.getUserByUserId(refreshTokenKeys.get("userId"));
        String accessToken = jwtService.generateAccessToken(u.getUserId(), u.getUsername(), TokenType.ACCESS);
        String refreshToken2 = jwtService.generateRefreshToken(u.getUserId(), u.getUsername(), TokenType.REFRESH);
        Map<String, String> refreshTokenKeys2 = jwtService.extractUserIdAndTokenIdFromToken(refreshToken2);
        authCacheProvider.storeRefreshToken(refreshTokenKeys2.get("userId"), refreshTokenKeys2.get("tokenId"), refreshToken);
        jwtService.setRefreshTokenCookie(response, refreshToken2);
        return accessToken;
    }

}
