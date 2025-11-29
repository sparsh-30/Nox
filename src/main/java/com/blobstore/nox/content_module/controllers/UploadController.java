package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.content_module.services.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final UploadService uploadService;

    @GetMapping("/start")
    public ResponseEntity<?> upload(){
        return uploadService.startUploadSession();
    }

    @PostMapping("/chunk")
    public ResponseEntity<?> uploadChunk(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("chunkNumber") Integer chunkNumber,
            @RequestParam("chunk") MultipartFile chunk
    ) throws IOException {
        return uploadService.uploadChunkToStorage(sessionId,chunkNumber,chunk);
    }

    @GetMapping("/merge/{sessionId}/{totalChunks}")
    public ResponseEntity<?> mergeChunks(
            @PathVariable String sessionId,
            @PathVariable Integer totalChunks
    ){
        return uploadService.mergeAllChunksIntoFinalFile(sessionId,totalChunks);
    }


}
