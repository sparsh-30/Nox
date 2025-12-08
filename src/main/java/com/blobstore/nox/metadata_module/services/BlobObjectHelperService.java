package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.exception_handling.ErrorCode;
import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
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

    public List<ReturnBlobObject> getAllChildrenByObjectId(Container parentContainer, String objectId) {
        BlobObject associatedBlobObject = validateObjectId(objectId);
        if (associatedBlobObject.getObjectType() == ObjectType.FILE) {
            return List.of();
        }
        String targetParentPath = generateParentPathFromObject(associatedBlobObject);
        return getAllChildrenOfParentPath(parentContainer, targetParentPath);
    }

    public List<ReturnBlobObject> getListOfDirectChildOfParentPath(Container parentContainer, String parentPath) {
        List<BlobObject> childObjects = blobObjectRepository.findByParentContainerAndParentPath(parentContainer, parentPath);
        return childObjects.stream().map(mapper::mapBlobObjectToDTO).toList();
    }

    public List<ReturnBlobObject> getAllChildrenOfParentPath(Container parentContainer, String parentPathKeyword) {
        List<BlobObject> childObjects = blobObjectRepository.findByParentContainerAndParentPathStartingWith(parentContainer, parentPathKeyword);
        return childObjects.stream().map(mapper::mapBlobObjectToDTO).toList();
    }

    public BlobObject validateObjectId(String objectId) {
        BlobObject associatedBlobObject = getBlobObjectFromObjectId(objectId);
        if (associatedBlobObject == null) {
            throw new CustomAppException("ObjectId is not valid", ErrorCode.INVALID_OBJECT_ID);
        }
        return associatedBlobObject;
    }

    public void validatePresenceOfSimilarObject(Container parentContainer, String objectName, String parentPath, ObjectType objectType) {
        Optional<BlobObject> associatedObject = blobObjectRepository.findByParentContainerAndObjectNameAndParentPathAndObjectType(parentContainer, objectName, parentPath, objectType);
        if (associatedObject.isPresent()) {
            throw new CustomAppException("Object already exists", ErrorCode.OBJECT_ALREADY_EXISTS);
        }
    }

    public BlobObject createNewBlobObject(String objectName, ObjectType objectType, Long size, String parentPath, Container parentContainer) {
        BlobObject newBlobObject = mapper.createBlobObjectEntity(UUIDGenerator.generateUUIDString(), objectName, objectType, size, parentPath, parentContainer);
        return blobObjectRepository.save(newBlobObject);
    }

    public void updateBlobObjectName(BlobObject blobObject, String updatedName) {
        blobObject.setObjectName(updatedName);
        saveBlobObject(blobObject);
    }


}
