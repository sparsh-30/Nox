package com.blobstore.nox.common.exception_handling;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
//    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED),
//    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
//
//    METADATA_NOT_FOUND(HttpStatus.NOT_FOUND),
//    METADATA_ALREADY_EXISTS(HttpStatus.CONFLICT),
//
//    UPLOAD_SESSION_INVALID(HttpStatus.BAD_REQUEST),
//    UPLOAD_CHUNK_MISSING(HttpStatus.BAD_REQUEST),
//
//    FILE_NOT_FOUND(HttpStatus.NOT_FOUND),
//    FILE_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
//
//    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    CACHE_NOT_FOUND(HttpStatus.BAD_REQUEST),

    INVALID_CONTAINER_ID(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }
}
