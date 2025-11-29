package com.blobstore.nox.content_module.services;

import com.blobstore.nox.content_module.utils.CacheProvider;
import com.blobstore.nox.files_module.services.FileOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final CacheProvider cacheProvider;
    private final FileOperationsService fileOperationsService;

    public ResponseEntity<String> startUploadSession() {
        // Generate sessionId
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        // Store the sessionId in cache
        cacheProvider.storeSessionId(uuidAsString);

        return ResponseEntity.ok(uuidAsString);
    }

    public ResponseEntity<String> uploadChunkToStorage(String sessionId, Integer chunkNumber, MultipartFile chunk) throws IOException {
        fileOperationsService.saveChunk(sessionId, chunkNumber, chunk);
        return ResponseEntity.ok("Chunk uploaded successfully");
    }

    public ResponseEntity<String> mergeAllChunksIntoFinalFile(String sessionId, Integer totalChunks) {
        String objectId = "testId";
        Path targetFilePath = fileOperationsService.generateFilePathFromObjectId(objectId);
        fileOperationsService.mergeChunksToFinalFile(targetFilePath, sessionId, totalChunks);
        return ResponseEntity.ok("File merged successfully.");
    }

    public ResponseEntity<String> mergeAllChunksToAnExistingFile(String objectId, String sessionId, Integer totalChunks){
        Path targetFilePath = fileOperationsService.generateFilePathFromObjectId(objectId);
        fileOperationsService.truncateFile(targetFilePath);
        fileOperationsService.mergeChunksToFinalFile(targetFilePath,sessionId,totalChunks);
        return ResponseEntity.ok("Successfully override the file.");
    }

}
