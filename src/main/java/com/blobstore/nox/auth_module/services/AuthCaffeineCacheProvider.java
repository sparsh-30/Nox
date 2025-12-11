package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.utils.AuthCacheProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCaffeineCacheProvider implements AuthCacheProvider {

    private final CacheManager cacheManager;

    private Cache getCache() {
        Cache cache = cacheManager.getCache("auth");
        if (cache == null) {
            throw new IllegalStateException("Cache 'auth' not configured!");
        }
        return cache;
    }

    public String combineUserIdAndTokenId(String userId, String tokenId) {
        return userId + ":" + tokenId;
    }

    @Override
    public String getStoredToken(String userId, String tokenId) {
        String key = combineUserIdAndTokenId(userId, tokenId);
        return getCache().get(key, String.class);
    }

    @Override
    public void storeRefreshToken(String userId, String tokenId, String refreshToken) {
        String key = combineUserIdAndTokenId(userId, tokenId);
        getCache().put(key, refreshToken);
    }

    @Override
    public void revokeRefreshToken(String userId, String tokenId) {
        String key = combineUserIdAndTokenId(userId, tokenId);
        getCache().evict(key);
    }

    @Override
    public Boolean isRefreshTokenPresent(String userId, String tokenId) {
        String key = combineUserIdAndTokenId(userId, tokenId);
        return getStoredToken(userId, tokenId) != null;
    }


}
