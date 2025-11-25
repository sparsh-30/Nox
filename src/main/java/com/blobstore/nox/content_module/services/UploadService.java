package com.blobstore.nox.content_module.services;

import com.blobstore.nox.content_module.utils.CacheProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final CacheProvider cacheProvider;

    public ResponseEntity<String> startUploadSession(){
        // Generate sessionId
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        // Store the sessionId in cache
        cacheProvider.storeSessionId(uuidAsString);

        return ResponseEntity.ok(uuidAsString);
    }

}
