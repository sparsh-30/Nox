package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.content_module.services.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final UploadService uploadService;

    @GetMapping("/start")
    public ResponseEntity<?> upload(){
        return uploadService.startUploadSession();
    }

}
