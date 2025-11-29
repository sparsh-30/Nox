package com.blobstore.nox.files_module.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileStorageInitializer {
    @Value("${file.storage.path}")
    private String storagePath;

    @PostConstruct
    public void init() {
        File directory = new File(storagePath);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created storage directory: " + storagePath);
            } else {
                System.err.println("Failed to create storage directory: " + storagePath);
            }
        } else {
            System.out.println("✔ Storage directory already exists: " + storagePath);
        }
    }
}
