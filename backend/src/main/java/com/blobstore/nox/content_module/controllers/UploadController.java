package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.common.response_handling.ApiResponse;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.content_module.services.MainUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final MainUploadService mainUploadService;

    @GetMapping("/start")
    public ResponseEntity<?> upload() {
        Map<String, Object> res = mainUploadService.startUploadSession();
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "Upload session created successfully", res));
    }

    @PostMapping("/chunk")
    public ResponseEntity<?> uploadChunk(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("chunkNumber") Integer chunkNumber,
            @RequestParam("chunk") MultipartFile chunk
    ) throws IOException {
        mainUploadService.uploadChunkToStorage(sessionId, chunkNumber, chunk);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "File chunk is saved successfully", null));
    }

    @PutMapping("/merge")
    public ResponseEntity<?> mergeChunks(
            @RequestParam String sessionId,
            @RequestParam Integer totalChunks,
            @RequestParam String objectName,
            @RequestParam Long size,
            @RequestParam String parentPath,
            @RequestParam String parentContainerId
    ) {
        Object res = mainUploadService.mergeAllChunksIntoFinalFile(sessionId, totalChunks, objectName, size, parentPath, parentContainerId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "File has been merged and saved successfully", res));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExisting(
            @RequestParam String objectId,
            @RequestParam String sessionId,
            @RequestParam Integer totalChunks
    ) {
        mainUploadService.mergeAllChunksToAnExistingFile(objectId, sessionId, totalChunks);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "File has been updated successfully", null));
    }


}
