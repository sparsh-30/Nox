package com.blobstore.nox.auth_module.controllers;

import com.blobstore.nox.auth_module.dtos.LoginRequest;
import com.blobstore.nox.auth_module.dtos.LoginResponse;
import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.dtos.RegisterResponse;
import com.blobstore.nox.auth_module.services.UserAuthService;
import com.blobstore.nox.common.response_handling.ApiResponse;
import com.blobstore.nox.common.response_handling.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody RegisterRequest registerRequest,
            HttpServletResponse response
    ) {
        RegisterResponse res = userAuthService.registerUser(response, registerRequest);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "User registered!!", res));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginResponse res = userAuthService.loginUser(response, loginRequest);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "User logged in!!", res));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(
            @RequestParam String userId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        userAuthService.logoutUser(request, response, userId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "User logged out!!", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String accessToken = userAuthService.refreshToken(request, response);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "User token is refreshed", accessToken));
    }

}
