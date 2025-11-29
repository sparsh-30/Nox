package com.blobstore.nox.content_module.utils;

import com.blobstore.nox.content_module.exceptions.InvalidUploadSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUploadSessionException.class)
    public ResponseEntity<?> handleInvalidSession(InvalidUploadSessionException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "INVALID_SESSION",
                        "message", ex.getMessage()
                ));
    }
}
