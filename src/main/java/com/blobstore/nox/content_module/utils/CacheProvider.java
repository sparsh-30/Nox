package com.blobstore.nox.content_module.utils;

public interface CacheProvider {

    void storeSessionId(String sessionId);

    void incrementNumberOfChunksForSession(String sessionId);

    void deleteSessionId(String sessionId);
}
