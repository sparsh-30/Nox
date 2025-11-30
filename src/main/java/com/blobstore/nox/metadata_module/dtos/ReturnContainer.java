package com.blobstore.nox.metadata_module.dtos;

import java.time.LocalDateTime;

public record ReturnContainer(
        String containerId,
        String containerName,
        LocalDateTime containerCreatedAt
) {
}
