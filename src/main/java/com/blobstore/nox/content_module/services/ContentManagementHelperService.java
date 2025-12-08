package com.blobstore.nox.content_module.services;

import com.blobstore.nox.files_module.services.FileOperationsService;
import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentManagementHelperService {

    private final FileOperationsService fileOperationsService;
    private final BlobObjectHelperService blobObjectHelperService;

    public void emptyAllChildrenOfParentPath(Container container, String parentPath) {
        List<ReturnBlobObject> allChildObjects = blobObjectHelperService.getAllChildrenOfParentPath(container, parentPath);
        for (ReturnBlobObject i : allChildObjects) {
            deleteAnObjectEntry(i);
        }
    }

    public void deleteAnObjectEntry(ReturnBlobObject targetObject) {
        if (targetObject.objectType() == ObjectType.FILE) {
            fileOperationsService.deleteFileByObjectId(targetObject.objectId());
        }
        blobObjectHelperService.deleteBlobObject(targetObject.objectId());
    }

}
