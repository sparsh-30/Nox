package com.blobstore.nox.metadata_module.utils;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.dtos.ReturnContainer;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
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

}
