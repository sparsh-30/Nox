package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.content_module.utils.CacheProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadHelperService {

    private final CacheProvider cacheProvider;

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
