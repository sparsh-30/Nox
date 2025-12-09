package com.blobstore.nox.auth_module.utils;

import com.blobstore.nox.auth_module.dtos.RegisterRequest;
import com.blobstore.nox.auth_module.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEntitiesMapper {

    // Generate a User entity
    public User mapRegisterRequestToUser(RegisterRequest registerRequest, String userId, String hashedPassword){
        return User.builder()
                .userId(userId)
                .username(registerRequest.username())
                .password(hashedPassword)
                .build();
    }

}
