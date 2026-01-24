package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.content_module.utils.ContentCacheProvider;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadHelperService {

    private final ContentCacheProvider cacheProvider;
    private final BlobObjectHelperService blobObjectHelperService;
    private final EntitiesMapper mapper;

    public BlobObject saveNewObject(String objectId, String objectName, Long size, String parentPath, String parentContainerPath){
        BlobObject bo = mapper.createBlobObjectEntity(objectId, objectName, ObjectType.FILE, size, parentPath, parentContainerPath);
        return blobObjectHelperService.saveBlobObject(bo);
    }

    public void validateSessionId(String sessionId) {
        if (cacheProvider.getValueStoredForSession(sessionId) == (-1)) {
            throw new CustomAppException("Session Invalid", ResponseCode.CACHE_NOT_FOUND);
        }
    }

    public void incrementChunkNumberInCache(String sessionId) {
        cacheProvider.incrementNumberOfChunksForSession(sessionId);
    }

    public void deleteSessionIdFromCache(String sessionId) {
        cacheProvider.deleteSessionId(sessionId);
    }

    public void addSessionIdToCache(String sessionId) {
        cacheProvider.storeSessionId(sessionId);
    }

}
