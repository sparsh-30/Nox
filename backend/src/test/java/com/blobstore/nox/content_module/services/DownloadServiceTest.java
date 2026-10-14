package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownloadServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    private BlobObjectHelperService blobObjectHelperService;

    @InjectMocks
    private DownloadService downloadService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(downloadService, "storagePath", tempDir.toString() + "/");
    }

    @Test
    void downloadFileFromObjectId_returnsPathWhenFileExists() throws Exception {
        String objectId = "obj-1";
        Files.writeString(tempDir.resolve(objectId + ".data"), "payload");
        BlobObject blob = BlobObject.builder()
                .objectId(objectId)
                .objectName("readme.txt")
                .objectType(ObjectType.FILE)
                .build();
        when(blobObjectHelperService.validateObjectId(objectId)).thenReturn(blob);

        Map<String, Object> result = downloadService.downloadFileFromObjectId(objectId);

        assertThat(result.get("objectName")).isEqualTo("readme.txt");
        assertThat((Path) result.get("path")).exists();
    }

    @Test
    void downloadFileFromObjectId_throwsWhenFileMissingOnDisk() {
        String objectId = "missing";
        BlobObject blob = BlobObject.builder().objectId(objectId).objectName("x.txt").build();
        when(blobObjectHelperService.validateObjectId(objectId)).thenReturn(blob);

        assertThatThrownBy(() -> downloadService.downloadFileFromObjectId(objectId))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.FILE_NOT_PRESENT);
    }
}
