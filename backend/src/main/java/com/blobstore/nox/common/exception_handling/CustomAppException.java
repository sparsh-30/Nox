package com.blobstore.nox.common.exception_handling;

import com.blobstore.nox.common.response_handling.ResponseCode;
import lombok.Getter;

@Getter
public class CustomAppException extends RuntimeException {

    private final ResponseCode responseCode;

    public CustomAppException(String message, ResponseCode responseCode){
        super(message);
        this.responseCode = responseCode;
    }

}
