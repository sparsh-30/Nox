package com.blobstore.nox.common.response_handling;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ResponseCode {

    // Success codes
    SUCCESS(true,HttpStatus.OK),


    // Temp code
    PLACEHOLDER(false,HttpStatus.BAD_REQUEST),


    // Error codes
    CACHE_NOT_FOUND(false,HttpStatus.BAD_REQUEST),
    INVALID_CONTAINER_ID(false,HttpStatus.BAD_REQUEST),
    OBJECT_ALREADY_EXISTS(false,HttpStatus.BAD_REQUEST),
    INVALID_OBJECT_ID(false,HttpStatus.BAD_REQUEST),
    AUTH_USER_ALREADY_EXISTS(false,HttpStatus.UNAUTHORIZED),
    AUTH_USER_NOT_FOUND(false,HttpStatus.UNAUTHORIZED),
    AUTH_INVALID_CREDENTIALS(false,HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_PRESENT(false,HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(false,HttpStatus.UNAUTHORIZED),
    USER_SESSION_ALREADY_REVOKED(false,HttpStatus.UNAUTHORIZED),
    HEADER_NOT_PRESENT(false,HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_NOT_VALID(false,HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED(false,HttpStatus.UNAUTHORIZED)


    ;

//    USER_NOT_FOUND(false,HttpStatus.NOT_FOUND),
//    BAD_REQUEST(false,HttpStatus.BAD_REQUEST),
//    INTERNAL_ERROR(false,HttpStatus.INTERNAL_SERVER_ERROR);

    private final Boolean success;
    private final HttpStatus status;

    ResponseCode(Boolean success,HttpStatus status) {
        this.success = success;
        this.status = status;
    }

}

