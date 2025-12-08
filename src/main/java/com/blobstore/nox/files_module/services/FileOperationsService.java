package com.blobstore.nox.files_module.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class FileOperationsService {

    @Value("${file.storage.path}")
    private String storagePath;

    @Value("${file.storage.buffer-size}")
    private int BUFFER_SIZE;

    public void saveChunk(String sessionId, Integer chunkNumber, MultipartFile chunk) {
        // Create temporary directory for sessionId
        File sessionDir = new File(storagePath + "/tmp");
        if (!sessionDir.exists()) {
            sessionDir.mkdirs();
        }

        // Save chunk to temp file
        File chunkFile = new File(sessionDir, sessionId + "_" + chunkNumber + ".part");
        try {
            chunk.transferTo(chunkFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path generateFilePathFromObjectId(String objectId) {
        return Paths.get(storagePath + objectId + ".data");
    }

    public void mergeChunksToFinalFile(Path targetFilePath, String sessionId, Integer totalChunks) {
        for (int i = 0; i < totalChunks; i++) {
            appendChunkToTargetFile(sessionId, i, targetFilePath);
        }
    }

    public void appendChunkToTargetFile(String sessionId, Integer chunkNumber, Path targetFilePath) {
        // Create the target file if it does not exist
        if (Files.notExists(targetFilePath)) {
            try {
                Files.createFile(targetFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Path chunkPath = Paths.get(storagePath + "/tmp/" + sessionId + "_" + chunkNumber + ".part");
        // Append contents of chunkFile into targetFile by reading in small buffers
        try (
                InputStream in = new BufferedInputStream(Files.newInputStream(chunkPath, StandardOpenOption.READ));
                OutputStream out = new BufferedOutputStream(Files.newOutputStream(targetFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND));
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();

            // Delete the chunk file
            deleteFile(chunkPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void truncateFile(Path targetFilePath) {
        try (
                FileOutputStream fos = new FileOutputStream(targetFilePath.toString(), false);
        ) {
            System.out.println("Truncated file: " + targetFilePath.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(Path targetFilePath) {
        try {
            Files.deleteIfExists(targetFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFileByObjectId(String objectId) {
        Path targetFilePath = generateFilePathFromObjectId(objectId);
        deleteFile(targetFilePath);
    }

}
