package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.exception_handling.ErrorCode;
import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.repositories.BlobObjectRepository;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlobObjectHelperService {

    private final BlobObjectRepository blobObjectRepository;
    private final EntitiesMapper mapper;

    public BlobObject saveBlobObject(BlobObject blobObject) {
        return blobObjectRepository.save(blobObject);
    }

    public void deleteBlobObject(String objectId) {
        blobObjectRepository.deleteById(objectId);
    }

    public BlobObject getBlobObjectFromObjectId(String objectId) {
        Optional<BlobObject> blobObject = blobObjectRepository.findByObjectId(objectId);
        return blobObject.orElse(null);
    }

    public String generateParentPathFromObject(BlobObject targetBlobObject) {
        return (targetBlobObject.getParentPath() + targetBlobObject.getObjectName() + "/");
    }

    public List<ReturnBlobObject> getAllChildrenByObjectId(String parentContainerId, String objectId) {
        BlobObject associatedBlobObject = validateObjectId(objectId);
        if (associatedBlobObject.getObjectType() == ObjectType.FILE) {
            return List.of();
        }
        String targetParentPath = generateParentPathFromObject(associatedBlobObject);
        return getAllChildrenOfParentPath(parentContainerId, targetParentPath);
    }

    public List<ReturnBlobObject> getListOfDirectChildOfParentPath(String parentContainerId, String parentPath) {
        List<BlobObject> childObjects = blobObjectRepository.findByParentContainerIdAndParentPath(parentContainerId, parentPath);
        return childObjects.stream().map(mapper::mapBlobObjectToDTO).toList();
    }

    public List<ReturnBlobObject> getAllChildrenOfParentPath(String parentContainerId, String parentPathKeyword) {
        List<BlobObject> childObjects = blobObjectRepository.findByParentContainerIdAndParentPathStartingWith(parentContainerId, parentPathKeyword);
        return childObjects.stream().map(mapper::mapBlobObjectToDTO).toList();
    }

    public BlobObject validateObjectId(String objectId) {
        BlobObject associatedBlobObject = getBlobObjectFromObjectId(objectId);
        if (associatedBlobObject == null) {
            throw new CustomAppException("ObjectId is not valid", ErrorCode.INVALID_OBJECT_ID);
        }
        return associatedBlobObject;
    }

    public void validatePresenceOfSimilarObject(String parentContainerId, String objectName, String parentPath, ObjectType objectType) {
        Optional<BlobObject> associatedObject = blobObjectRepository.findByParentContainerIdAndObjectNameAndParentPathAndObjectType(parentContainerId, objectName, parentPath, objectType);
        if (associatedObject.isPresent()) {
            throw new CustomAppException("Object already exists", ErrorCode.OBJECT_ALREADY_EXISTS);
        }
    }

    public BlobObject createNewBlobObject(String objectName, ObjectType objectType, Long size, String parentPath, String parentContainerId) {
        BlobObject newBlobObject = mapper.createBlobObjectEntity(UUIDGenerator.generateUUIDString(), objectName, objectType, size, parentPath, parentContainerId);
        return blobObjectRepository.save(newBlobObject);
    }

    public void updateBlobObjectName(BlobObject blobObject, String updatedName) {
        blobObject.setObjectName(updatedName);
        saveBlobObject(blobObject);
    }


}
