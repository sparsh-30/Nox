package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.exception_handling.ErrorCode;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.repositories.BlobObjectRepository;
import com.blobstore.nox.metadata_module.repositories.ContainerRepository;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlobObjectService {

    private final BlobObjectRepository blobObjectRepository;
    private final ContainerRepository containerRepository;
    private final EntitiesMapper mapper;

    public ResponseEntity<String> saveObject(String containerId, String objectName, ObjectType objectType, Long size, String parentPath){
        // Fetch the associated container from database
        Optional<Container> associatedContainer = containerRepository.findByContainerId(containerId);

        // Check if the containerId is valid or not
        if(associatedContainer.isEmpty()){
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Check is the object with same name and parent path exists in the same container or not
        if(blobObjectRepository.findByParentContainerAndObjectNameAndParentPathAndObjectType(associatedContainer.get(),objectName,parentPath, objectType).isPresent()){
            throw new CustomAppException("Object already exists",ErrorCode.OBJECT_ALREADY_EXISTS);
        }

        // Generate BlobObject entity
        BlobObject tempBlobObject = mapper.createBlobObjectEntity(UUIDGenerator.generateUUIDString(),objectName,objectType,size,parentPath,associatedContainer.get());

        // Make a save request to database to add BlobObject entry
        BlobObject savedObject = blobObjectRepository.save(tempBlobObject);

        return ResponseEntity.ok(savedObject.getObjectId());
    }


}
