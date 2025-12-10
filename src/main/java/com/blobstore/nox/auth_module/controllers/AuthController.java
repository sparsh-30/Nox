package com.blobstore.nox.auth_module.controllers;

import com.blobstore.nox.auth_module.dtos.LoginRequest;
import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.dtos.RegisterResponse;
import com.blobstore.nox.auth_module.services.UserAuthService;
import com.blobstore.nox.common.response_handling.ApiResponse;
import com.blobstore.nox.common.response_handling.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1/")
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody RegisterRequest registerRequest
    ) {
        RegisterResponse res = userAuthService.registerUser(registerRequest);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(ResponseCode.SUCCESS.getSuccess(), ResponseCode.SUCCESS, "User registered!!", res));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(userAuthService.loginUser(loginRequest));
    }

}
