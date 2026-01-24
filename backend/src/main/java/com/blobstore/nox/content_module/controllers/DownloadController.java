package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.content_module.services.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/download")
public class DownloadController {

    private final DownloadService downloadService;

    @GetMapping
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String objectId
    ) throws IOException {
        Map<String, Object> res = downloadService.downloadFileFromObjectId(objectId);
        String objectName = (String)res.get("objectName");
        Path path = (Path)res.get("path");
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + objectName + "\""
                )
                .contentLength(Files.size(path))
                .body(resource);
    }

}
