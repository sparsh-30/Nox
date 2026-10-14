package com.blobstore.nox.files_module.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileOperationsServiceTest {

    @TempDir
    Path tempDir;

    private FileOperationsService fileOperationsService;

    @BeforeEach
    void setUp() {
        fileOperationsService = new FileOperationsService();
        ReflectionTestUtils.setField(fileOperationsService, "storagePath", tempDir.toString() + "/");
        ReflectionTestUtils.setField(fileOperationsService, "BUFFER_SIZE", 1024);
    }

    @Test
    void saveChunkAndMergeChunksToFinalFile_producesMergedFile() throws Exception {
        String sessionId = "upload-session";
        MockMultipartFile chunk0 = new MockMultipartFile("chunk", "part0", "application/octet-stream", "hello ".getBytes());
        MockMultipartFile chunk1 = new MockMultipartFile("chunk", "part1", "application/octet-stream", "world".getBytes());

        fileOperationsService.saveChunk(sessionId, 0, chunk0);
        fileOperationsService.saveChunk(sessionId, 1, chunk1);

        Path target = fileOperationsService.generateFilePathFromObjectId("obj-1");
        fileOperationsService.mergeChunksToFinalFile(target, sessionId, 2);

        assertThat(Files.readString(target)).isEqualTo("hello world");
        assertThat(Files.exists(tempDir.resolve("tmp"))).isTrue();
    }

    @Test
    void deleteFileByObjectId_removesDataFile() throws Exception {
        Path target = fileOperationsService.generateFilePathFromObjectId("del-me");
        Files.writeString(target, "data");

        fileOperationsService.deleteFileByObjectId("del-me");

        assertThat(Files.exists(target)).isFalse();
    }

    @Test
    void truncateFile_clearsExistingContent() throws Exception {
        Path target = fileOperationsService.generateFilePathFromObjectId("truncate-me");
        Files.writeString(target, "old-content");

        fileOperationsService.truncateFile(target);

        assertThat(Files.size(target)).isZero();
    }
}
