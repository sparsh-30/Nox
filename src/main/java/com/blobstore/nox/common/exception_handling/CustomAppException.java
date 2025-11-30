package com.blobstore.nox.common.exception_handling;

import lombok.Getter;

@Getter
public class CustomAppException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomAppException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
