package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainBlobObjectService {

    private final ContainerHelperService containerHelperService;
    private final BlobObjectHelperService blobObjectHelperService;
    private final EntitiesMapper mapper;

    public String saveObject(String containerId, String objectName, ObjectType objectType, Long size, String parentPath) {
        Container container = containerHelperService.validateContainerId(containerId);
        blobObjectHelperService.validatePresenceOfSimilarObject(container, objectName, parentPath, objectType);
        BlobObject newBlobObject = blobObjectHelperService.createNewBlobObject(objectName, objectType, size, parentPath, container);
        return newBlobObject.getObjectId();
    }

    public String updateObjectName(String containerId, String objectId, String updatedName) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        BlobObject associatedBlobObject = blobObjectHelperService.validateObjectId(objectId);
        blobObjectHelperService.validatePresenceOfSimilarObject(associatedContainer, updatedName, associatedBlobObject.getParentPath(), associatedBlobObject.getObjectType());
        blobObjectHelperService.updateBlobObjectName(associatedBlobObject, updatedName);
        return "Name updated!!";
    }

    public List<ReturnBlobObject> getAllObjects(String containerId, String parentPath) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        List<BlobObject> childObjects = blobObjectHelperService.getListOfDirectChildOfParentPath(associatedContainer, parentPath);
        return childObjects.stream().map(mapper::mapBlobObjectToDTO).toList();
    }

    public List<String> getAllDirectObjects(String containerId, String parentPathKeyword) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        List<BlobObject> childBlobObjectsList = blobObjectHelperService.getAllChildrenOfParentPath(associatedContainer, parentPathKeyword);
        return childBlobObjectsList.stream().map(BlobObject::getObjectId).toList();
    }

}
