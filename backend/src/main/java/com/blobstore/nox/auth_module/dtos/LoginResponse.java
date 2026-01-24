package com.blobstore.nox.auth_module.dtos;

public record LoginResponse(
        String userId,
        String username,
        String accessToken
) {
}
