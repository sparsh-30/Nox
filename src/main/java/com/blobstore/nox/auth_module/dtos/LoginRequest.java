package com.blobstore.nox.auth_module.dtos;

public record LoginRequest(
        String username,
        String password
) {
}
