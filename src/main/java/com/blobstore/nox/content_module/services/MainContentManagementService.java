package com.blobstore.nox.content_module.services;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import com.blobstore.nox.metadata_module.services.ContainerHelperService;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainContentManagementService {

    private final ContentManagementHelperService contentManagementHelperService;
    private final ContainerHelperService containerHelperService;
    private final BlobObjectHelperService blobObjectHelperService;
    private final EntitiesMapper mapper;

    public String emptyAllChildrenOfParentPath(String containerId, String parentPath) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        contentManagementHelperService.emptyAllChildrenOfParentPath(associatedContainer, parentPath);
        return "Parent path is emptied!!";
    }

    public String deleteAnObject(String containerId, String objectId) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        BlobObject associatedBlobObject = blobObjectHelperService.validateObjectId(objectId);
        List<ReturnBlobObject> temp = blobObjectHelperService.getAllChildrenByObjectId(associatedContainer, objectId);
        if(!temp.isEmpty()){
            String targetParentPath = blobObjectHelperService.generateParentPathFromObject(associatedBlobObject);
            contentManagementHelperService.emptyAllChildrenOfParentPath(associatedContainer,targetParentPath);
        }
        contentManagementHelperService.deleteAnObjectEntry(mapper.mapBlobObjectToDTO(associatedBlobObject));
        return "Object has been deleted!!";
    }

}
