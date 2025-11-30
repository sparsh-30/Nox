package com.blobstore.nox.metadata_module.dtos;

import com.blobstore.nox.metadata_module.models.ObjectType;

import java.time.LocalDateTime;

public record ReturnBlobObject(
        String objectId,
        String objectName,
        ObjectType objectType,
        Long size,
        String parentPath,
        LocalDateTime objectCreatedAt,
        String parentContainerId
) {
}
