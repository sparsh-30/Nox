package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.dtos.LoginRequest;
import com.blobstore.nox.auth_module.dtos.LoginResponse;
import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.dtos.RegisterResponse;
import com.blobstore.nox.auth_module.models.User;
import com.blobstore.nox.auth_module.utils.AuthEntitiesMapper;
import com.blobstore.nox.auth_module.utils.TokenType;
import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthHelperService authHelperService;
    private final AuthEntitiesMapper mapper;
    private final JWTService jwtService;

    public RegisterResponse registerUser(HttpServletResponse response, RegisterRequest registerRequest) {
        if (authHelperService.doesUserExistByUsername(registerRequest.username())) {
            throw new CustomAppException("Username already exists", ResponseCode.AUTH_USER_ALREADY_EXISTS);
        }
        String hashedPassword = authHelperService.getHashedPassword(registerRequest.password());
        User u = mapper.mapRegisterRequestToUser(registerRequest, UUIDGenerator.generateUUIDString(), hashedPassword);
        User savedUser = authHelperService.saveUserEntity(u);
        String accessToken = jwtService.generateAccessToken(savedUser.getUserId(), savedUser.getUsername(), TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(savedUser.getUserId(), savedUser.getUsername(), TokenType.REFRESH);
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
        jwtService.setRefreshTokenCookie(response, refreshToken);
        return mapper.generateLoginResponse(u, accessToken);
    }

}
