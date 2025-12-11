package com.blobstore.nox.auth_module.utils;

public interface AuthCacheProvider {

    String getStoredToken(String userId, String tokenId);

    void storeRefreshToken(String userId, String tokenId, String refreshToken);

    void revokeRefreshToken(String userId, String tokenId);
}
