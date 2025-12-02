package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.content_module.services.MainUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final MainUploadService mainUploadService;

    @GetMapping("/start")
    public ResponseEntity<?> upload() {
        String uploadSessionId = mainUploadService.startUploadSession();
        return ResponseEntity.ok(uploadSessionId);
    }

    @PostMapping("/chunk")
    public ResponseEntity<?> uploadChunk(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("chunkNumber") Integer chunkNumber,
            @RequestParam("chunk") MultipartFile chunk
    ) throws IOException {
        return ResponseEntity.ok(mainUploadService.uploadChunkToStorage(sessionId, chunkNumber, chunk));
    }

    @PutMapping("/merge")
    public ResponseEntity<?> mergeChunks(
            @RequestParam String sessionId,
            @RequestParam Integer totalChunks
    ) {
        return ResponseEntity.ok(mainUploadService.mergeAllChunksIntoFinalFile(sessionId, totalChunks));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExisting(
            @RequestParam String objectId,
            @RequestParam String sessionId,
            @RequestParam Integer totalChunks
    ) {
        return ResponseEntity.ok(mainUploadService.mergeAllChunksToAnExistingFile(objectId, sessionId, totalChunks));
    }


}
