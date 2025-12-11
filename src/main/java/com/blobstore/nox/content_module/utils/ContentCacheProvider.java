package com.blobstore.nox.content_module.utils;

public interface ContentCacheProvider {

    Integer getValueStoredForSession(String sessionId);

    void storeSessionId(String sessionId);

    void incrementNumberOfChunksForSession(String sessionId);

    void deleteSessionId(String sessionId);
}
