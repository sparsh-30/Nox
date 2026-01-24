package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.files_module.services.FileOperationsService;
import com.blobstore.nox.metadata_module.models.BlobObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainUploadService {

    private final FileOperationsService fileOperationsService;
    private final UploadHelperService uploadHelperService;

    public Map<String, Object> startUploadSession() {
        String uuidString = UUIDGenerator.generateUUIDString();
        uploadHelperService.addSessionIdToCache(uuidString);
        return Map.of(
                "sessionId", uuidString
        );
    }

    public void uploadChunkToStorage(String sessionId, Integer chunkNumber, MultipartFile chunk) throws IOException {
        uploadHelperService.validateSessionId(sessionId);
        fileOperationsService.saveChunk(sessionId, chunkNumber, chunk);
        uploadHelperService.incrementChunkNumberInCache(sessionId);
    }

    public BlobObject mergeAllChunksIntoFinalFile(String sessionId, Integer totalChunks, String objectName, Long size, String parentPath, String parentContainerId) {
        uploadHelperService.validateSessionId(sessionId);

        String objectId = UUIDGenerator.generateUUIDString();
        Path targetFilePath = fileOperationsService.generateFilePathFromObjectId(objectId);
        fileOperationsService.mergeChunksToFinalFile(targetFilePath, sessionId, totalChunks);
        BlobObject savedObject = uploadHelperService.saveNewObject(objectId, objectName, size, parentPath, parentContainerId);
        uploadHelperService.deleteSessionIdFromCache(sessionId);
        return savedObject;
    }

    public void mergeAllChunksToAnExistingFile(String objectId, String sessionId, Integer totalChunks) {
        uploadHelperService.validateSessionId(sessionId);
        Path targetFilePath = fileOperationsService.generateFilePathFromObjectId(objectId);
        fileOperationsService.truncateFile(targetFilePath);
        fileOperationsService.mergeChunksToFinalFile(targetFilePath, sessionId, totalChunks);
        uploadHelperService.deleteSessionIdFromCache(sessionId);
    }

}
