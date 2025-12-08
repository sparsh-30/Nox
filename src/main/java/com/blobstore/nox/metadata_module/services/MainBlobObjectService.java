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
        Container targetContainer = containerHelperService.validateContainerId(containerId);
        String targetContainerId = containerHelperService.getTargetContainerId(targetContainer);
        blobObjectHelperService.validatePresenceOfSimilarObject(targetContainerId, objectName, parentPath, objectType);
        BlobObject newBlobObject = blobObjectHelperService.createNewBlobObject(objectName, objectType, size, parentPath, targetContainerId);
        return newBlobObject.getObjectId();
    }

    public String updateObjectName(String containerId, String objectId, String updatedName) {
        Container targetContainer = containerHelperService.validateContainerId(containerId);
        String targetContainerId = containerHelperService.getTargetContainerId(targetContainer);
        BlobObject targetBlobObject = blobObjectHelperService.validateObjectId(objectId);
        blobObjectHelperService.validatePresenceOfSimilarObject(targetContainerId, updatedName, targetBlobObject.getParentPath(), targetBlobObject.getObjectType());
        blobObjectHelperService.updateBlobObjectName(targetBlobObject, updatedName);
        return "Name updated!!";
    }

    public List<ReturnBlobObject> getAllObjects(String containerId, String parentPath) {
        Container targetContainer = containerHelperService.validateContainerId(containerId);
        String targetContainerId = containerHelperService.getTargetContainerId(targetContainer);
        return blobObjectHelperService.getListOfDirectChildOfParentPath(targetContainerId, parentPath);
    }

    public List<String> getAllDirectObjects(String containerId, String parentPathKeyword) {
        Container targetContainer = containerHelperService.validateContainerId(containerId);
        String targetContainerId = containerHelperService.getTargetContainerId(targetContainer);
        List<ReturnBlobObject> childBlobObjectsList = blobObjectHelperService.getAllChildrenOfParentPath(targetContainerId, parentPathKeyword);
        return childBlobObjectsList.stream().map(ReturnBlobObject::objectId).toList();
    }

}
