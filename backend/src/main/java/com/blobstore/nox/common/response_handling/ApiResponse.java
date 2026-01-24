package com.blobstore.nox.common.response_handling;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(
        Boolean success,
        ResponseCode code,
        String message,
        Object payload,
        Instant timestamp
) {
    public ApiResponse(Boolean success,ResponseCode code, String message, Object payload) {
        this(success, code, message, payload, Instant.now());
    }
}

