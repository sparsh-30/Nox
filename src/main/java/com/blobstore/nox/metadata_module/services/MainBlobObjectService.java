package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.exception_handling.ErrorCode;
import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.repositories.BlobObjectRepository;
import com.blobstore.nox.metadata_module.repositories.ContainerRepository;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainBlobObjectService {

    private final BlobObjectRepository blobObjectRepository;
    private final ContainerRepository containerRepository;
    private final EntitiesMapper mapper;

    public String saveObject(String containerId, String objectName, ObjectType objectType, Long size, String parentPath) {
        // Fetch the associated container from database
        Optional<Container> associatedContainer = containerRepository.findByContainerId(containerId);

        // Check if the containerId is valid or not
        if (associatedContainer.isEmpty()) {
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Check is the object with same name and parent path exists in the same container or not
        if (blobObjectRepository.findByParentContainerAndObjectNameAndParentPathAndObjectType(associatedContainer.get(), objectName, parentPath, objectType).isPresent()) {
            throw new CustomAppException("Object already exists", ErrorCode.OBJECT_ALREADY_EXISTS);
        }

        // Generate BlobObject entity
        BlobObject tempBlobObject = mapper.createBlobObjectEntity(UUIDGenerator.generateUUIDString(), objectName, objectType, size, parentPath, associatedContainer.get());

        // Make a save request to database to add BlobObject entry
        BlobObject savedObject = blobObjectRepository.save(tempBlobObject);

        return savedObject.getObjectId();
    }

    public String updateObjectName(String containerId, String objectId, String updatedName) {
        // Fetch the associated container from database
        Optional<Container> associatedContainer = containerRepository.findByContainerId(containerId);

        // Check if the containerId is valid or not
        if (associatedContainer.isEmpty()) {
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Fetch the associated blobObject from database
        Optional<BlobObject> associatedBlobObject = blobObjectRepository.findByObjectId(objectId);

        // Check if the objectId is valid or not
        if (associatedBlobObject.isEmpty()) {
            throw new CustomAppException("ObjectId is not valid", ErrorCode.INVALID_OBJECT_ID);
        }

        // Check is the object with updated name and parent path exists in the same container or not
        if (blobObjectRepository.findByParentContainerAndObjectNameAndParentPathAndObjectType(associatedContainer.get(), updatedName, associatedBlobObject.get().getParentPath(), associatedBlobObject.get().getObjectType()).isPresent()) {
            throw new CustomAppException("Object already exists", ErrorCode.OBJECT_ALREADY_EXISTS);
        }

        // Save the object with updated name
        associatedBlobObject.get().setObjectName(updatedName);
        blobObjectRepository.save(associatedBlobObject.get());

        return "Name updated!!";
    }

    public List<ReturnBlobObject> getAllObjectsBelongingToParentPath(String containerId, String parentPath) {
        // Fetch the associated container from database
        Optional<Container> associatedContainer = containerRepository.findByContainerId(containerId);

        // Check if the containerId is valid or not
        if (associatedContainer.isEmpty()) {
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Get the list of child objects from database
        List<BlobObject> childObjects = blobObjectRepository.findByParentContainerAndParentPath(associatedContainer.get(), parentPath);

        // Convert list of child objects to returnable DTO object and return
        return childObjects.stream().map(mapper::mapBlobObjectToDTO).toList();
    }

    public List<String> getAllChildObjectsBelongingToObjectId(String containerId, String parentPathKeyword) {
        // Fetch the associated container from database
        Optional<Container> associatedContainer = containerRepository.findByContainerId(containerId);

        // Check if the containerId is valid or not
        if (associatedContainer.isEmpty()) {
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Find all the objects that have starting parent path as the provided value
        List<BlobObject> childBlobObjectsList = blobObjectRepository.findByParentPathStartingWith(parentPathKeyword);

        // Generate list of objectIds from the object list and return
        return childBlobObjectsList.stream().map(
                BlobObject::getObjectId
        ).toList();
    }


}
