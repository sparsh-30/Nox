package com.blobstore.nox.content_module.services;

import com.blobstore.nox.files_module.services.FileOperationsService;
import com.blobstore.nox.metadata_module.models.BlobObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainUploadServiceTest {

    @Mock
    private FileOperationsService fileOperationsService;
    @Mock
    private UploadHelperService uploadHelperService;
    @Mock
    private MultipartFile chunk;

    @InjectMocks
    private MainUploadService mainUploadService;

    @Test
    void startUploadSession_returnsSessionIdAndCachesIt() {
        var result = mainUploadService.startUploadSession();

        assertThat(result).containsKey("sessionId");
        assertThat(result.get("sessionId")).isNotNull();
        verify(uploadHelperService).addSessionIdToCache((String) result.get("sessionId"));
    }

    @Test
    void uploadChunkToStorage_validatesSessionAndIncrementsChunk() throws Exception {
        mainUploadService.uploadChunkToStorage("sess-1", 0, chunk);

        verify(uploadHelperService).validateSessionId("sess-1");
        verify(fileOperationsService).saveChunk("sess-1", 0, chunk);
        verify(uploadHelperService).incrementChunkNumberInCache("sess-1");
    }

    @Test
    void mergeAllChunksIntoFinalFile_mergesAndClearsSession() {
        Path targetPath = Paths.get("/tmp/obj.data");
        BlobObject saved = BlobObject.builder().objectId("obj-1").build();
        when(fileOperationsService.generateFilePathFromObjectId(anyString()))
                .thenReturn(targetPath);
        when(uploadHelperService.saveNewObject(
                anyString(),
                eq("file.txt"),
                eq(100L),
                eq("/"),
                eq("c1")
        )).thenReturn(saved);

        BlobObject result = mainUploadService.mergeAllChunksIntoFinalFile(
                "sess-1", 2, "file.txt", 100L, "/", "c1");

        assertThat(result).isSameAs(saved);
        verify(uploadHelperService).validateSessionId("sess-1");
        verify(fileOperationsService).mergeChunksToFinalFile(targetPath, "sess-1", 2);
        verify(uploadHelperService).deleteSessionIdFromCache("sess-1");
    }
}
