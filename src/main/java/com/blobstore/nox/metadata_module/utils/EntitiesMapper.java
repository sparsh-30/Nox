package com.blobstore.nox.metadata_module.utils;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.dtos.ReturnContainer;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import org.springframework.stereotype.Component;

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
                blobObject.getParentContainer().getContainerId()
        );
    }

    // Return Container entity from specified fields
    public Container createContainerEntity(
            String containerName,
            String containerUUID
    ) {
        return Container.builder()
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
            Container parentContainer
    ) {
        return BlobObject.builder()
                .objectId(objectId)
                .objectName(objectName)
                .objectType(objectType)
                .size(size)
                .parentPath(parentPath)
                .parentContainer(parentContainer)
                .build();
    }

}
