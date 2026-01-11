package com.blobstore.nox.metadata_module.utils;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.dtos.ReturnContainer;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EntitiesMapper {

    // Map: Container -> ReturnContainer
    public ReturnContainer mapContainerToDTO(Container container) {
        return new ReturnContainer(
                container.getContainerId(),
                container.getContainerName(),
                container.getContainerCreatedAt()
        );
    }

    // Map: BlobObject -> ReturnBlobObject
    public ReturnBlobObject mapBlobObjectToDTO(BlobObject blobObject) {
        return new ReturnBlobObject(
                blobObject.getObjectId(),
                blobObject.getObjectName(),
                blobObject.getObjectType(),
                blobObject.getSize(),
                blobObject.getParentPath(),
                blobObject.getObjectCreatedAt(),
                blobObject.getParentContainerId()
        );
    }

    // Return Container entity from specified fields
    public Container createContainerEntity(
            String userId,
            String containerName,
            String containerUUID
    ) {
        return Container.builder()
                .userId(userId)
                .containerId(containerUUID)
                .containerName(containerName)
                .build();
    }

    // Return BlobObject entity from specified fields
    public BlobObject createBlobObjectEntity(
            String objectId,
            String objectName,
            ObjectType objectType,
            Long size,
            String parentPath,
            String parentContainerId
    ) {
        return BlobObject.builder()
                .objectId(objectId)
                .objectName(objectName)
                .objectType(objectType)
                .size(size)
                .parentPath(parentPath)
                .parentContainerId(parentContainerId)
                .build();
    }

    // Map: Container -> Map
    public Map<String, Object> mapContainerToMap(Container container) {
        return Map.of(
                "userId", container.getUserId(),
                "containerId", container.getContainerId(),
                "containerName", container.getContainerName(),
                "containerCreatedAt", container.getContainerCreatedAt()
        );
    }

    // Map: BlobObject -> Map
    public Map<String, Object> MapBlobObjectToMap(BlobObject blobObject) {
        return Map.of(
                "objectId", blobObject.getObjectId(),
                "objectName", blobObject.getObjectName(),
                "objectType", blobObject.getObjectType(),
                "size", blobObject.getSize(),
                "parentPath", blobObject.getParentPath(),
                "objectCreatedAt", blobObject.getObjectCreatedAt(),
                "parentContainerId", blobObject.getParentContainerId()
        );
    }

}
