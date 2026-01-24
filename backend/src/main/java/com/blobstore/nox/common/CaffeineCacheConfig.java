package com.blobstore.nox.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CaffeineCacheConfig {

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationTime;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        CaffeineCache sessionsCache = new CaffeineCache(
                "sessions",
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .maximumSize(10_000)
                        .build()
        );

        CaffeineCache authCache = new CaffeineCache(
                "auth",
                Caffeine.newBuilder()
                        .expireAfterWrite(refreshTokenExpirationTime, TimeUnit.SECONDS)
                        .maximumSize(10_000)
                        .build()
        );

        cacheManager.setCaches(List.of(sessionsCache, authCache));
        return cacheManager;
    }

}
