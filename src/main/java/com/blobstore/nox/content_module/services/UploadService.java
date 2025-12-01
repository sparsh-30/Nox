package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.exception_handling.ErrorCode;
import com.blobstore.nox.content_module.utils.CacheProvider;
import com.blobstore.nox.files_module.services.FileOperationsService;
import lombok.RequiredArgsConstructor;
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

    public String startUploadSession() {
        // Generate sessionId
        String uuidString = UUIDGenerator.generateUUIDString();

        // Store the sessionId in cache
        cacheProvider.storeSessionId(uuidString);

        return uuidString;
    }

    public String uploadChunkToStorage(String sessionId, Integer chunkNumber, MultipartFile chunk) throws IOException {
        // Check if the sessionId is valid or not
        if (cacheProvider.getValueStoredForSession(sessionId)==(-1)){
            throw new CustomAppException("Session Invalid",ErrorCode.CACHE_NOT_FOUND);
        }

        // Save the chunk to storage
        fileOperationsService.saveChunk(sessionId, chunkNumber, chunk);

        // Update the chunk number in cache
        cacheProvider.incrementNumberOfChunksForSession(sessionId);

        return "Chunk uploaded successfully!!";
    }

    public String mergeAllChunksIntoFinalFile(String sessionId, Integer totalChunks) {
        // Check if the sessionId is valid or not
        if (cacheProvider.getValueStoredForSession(sessionId)==(-1)){
            throw new CustomAppException("Session Invalid",ErrorCode.CACHE_NOT_FOUND);
        }

        // Obtain the objectId
        String objectId = "testId";

        // Generate targetFilePath
        Path targetFilePath = fileOperationsService.generateFilePathFromObjectId(objectId);

        // Merge all chunks related to the sessionId into a single file
        fileOperationsService.mergeChunksToFinalFile(targetFilePath, sessionId, totalChunks);

        // Evict the sessionId from cache
        cacheProvider.deleteSessionId(sessionId);

        return "File merged successfully!!";
    }

    public String mergeAllChunksToAnExistingFile(String objectId, String sessionId, Integer totalChunks){
        // Check if the sessionId is valid or not
        if (cacheProvider.getValueStoredForSession(sessionId)==(-1)){
            throw new CustomAppException("Session Invalid",ErrorCode.CACHE_NOT_FOUND);
        }

        // Generate targetFilePath
        Path targetFilePath = fileOperationsService.generateFilePathFromObjectId(objectId);

        // Truncate the earlier contents of existing file
        fileOperationsService.truncateFile(targetFilePath);

        // Merge all chunks related to the sessionId into a single file
        fileOperationsService.mergeChunksToFinalFile(targetFilePath,sessionId,totalChunks);

        // Evict the sessionId from cache
        cacheProvider.deleteSessionId(sessionId);

        return "Successfully override the file";
    }

}
