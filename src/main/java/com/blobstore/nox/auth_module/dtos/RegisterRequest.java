package com.blobstore.nox.auth_module.dtos;

public record RegisterRequest(
        String username,
        String password
) {
}
