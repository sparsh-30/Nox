package com.blobstore.nox.auth_module.dtos;

public record RegisterResponse(
        String userId,
        String username,
        String accessToken
) {
}
