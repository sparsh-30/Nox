package com.blobstore.nox.content_module.services;

import com.blobstore.nox.content_module.utils.CacheProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaffeineCacheProvider implements CacheProvider {

    private final CacheManager cacheManager;

    private Cache getCache() {
        Cache cache = cacheManager.getCache("sessions");
        if (cache == null) {
            throw new IllegalStateException("Cache 'sessions' not configured!");
        }
        return cache;
    }

    @Override
    public void storeSessionId(String sessionId) {
        getCache().put(sessionId,0);
    }

    @Override
    public void incrementNumberOfChunksForSession(String sessionId) {
        Integer currVal = getCache().get(sessionId, Integer.class);
        if (currVal == null) {
            currVal = 0;
        }
        getCache().put(sessionId,currVal+1);
    }

    @Override
    public void deleteSessionId(String sessionId){
        getCache().evict(sessionId);
    }
}
