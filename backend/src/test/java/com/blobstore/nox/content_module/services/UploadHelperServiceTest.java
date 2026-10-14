package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.content_module.utils.ContentCacheProvider;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadHelperServiceTest {

    @Mock
    private ContentCacheProvider cacheProvider;
    @Mock
    private BlobObjectHelperService blobObjectHelperService;
    @Mock
    private EntitiesMapper mapper;

    @InjectMocks
    private UploadHelperService uploadHelperService;

    @Test
    void validateSessionId_throwsWhenSessionMissing() {
        when(cacheProvider.getValueStoredForSession("bad")).thenReturn(-1);

        assertThatThrownBy(() -> uploadHelperService.validateSessionId("bad"))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.CACHE_NOT_FOUND);
    }

    @Test
    void saveNewObject_createsEntityAndPersists() {
        BlobObject entity = BlobObject.builder().objectId("o1").build();
        when(mapper.createBlobObjectEntity("o1", "file.txt", ObjectType.FILE, 10L, "/", "c1"))
                .thenReturn(entity);
        when(blobObjectHelperService.saveBlobObject(entity)).thenReturn(entity);

        BlobObject saved = uploadHelperService.saveNewObject("o1", "file.txt", 10L, "/", "c1");

        assertThat(saved).isSameAs(entity);
        verify(blobObjectHelperService).saveBlobObject(entity);
    }

    @Test
    void addSessionIdToCache_delegatesToProvider() {
        uploadHelperService.addSessionIdToCache("sess-1");
        verify(cacheProvider).storeSessionId("sess-1");
    }
}
