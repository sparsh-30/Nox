package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.dtos.LoginRequest;
import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.dtos.RegisterResponse;
import com.blobstore.nox.auth_module.models.User;
import com.blobstore.nox.auth_module.utils.AuthEntitiesMapper;
import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthHelperService authHelperService;
    private final AuthEntitiesMapper mapper;

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if (authHelperService.doesUserExistByUsername(registerRequest.username())) {
            throw new CustomAppException("Username already exists", ResponseCode.AUTH_USER_ALREADY_EXISTS);
        }
        String hashedPassword = authHelperService.getHashedPassword(registerRequest.password());
        User u = mapper.mapRegisterRequestToUser(registerRequest, UUIDGenerator.generateUUIDString(), hashedPassword);
        User savedUser = authHelperService.saveUserEntity(u);
        return mapper.generateRegisterResponse(savedUser, "klsdf");
    }

    public String loginUser(LoginRequest loginRequest) {
        User u = authHelperService.getUserByUsername(loginRequest.username());
        if (u == null) {
            throw new CustomAppException("User not found", ResponseCode.AUTH_USER_NOT_FOUND);
        }
        if (!authHelperService.doPasswordsMatch(loginRequest.password(), u.getPassword())) {
            throw new CustomAppException("Invalid credentials", ResponseCode.AUTH_INVALID_CREDENTIALS);
        }
        return "User logged in!!";
    }

}
