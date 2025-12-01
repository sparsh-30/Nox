package com.blobstore.nox.metadata_module.controllers;

import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/object")
public class BlobObjectController {

    private final BlobObjectService blobObjectService;

    @GetMapping("/create")
    public ResponseEntity<?> createObject(
            @RequestParam String containerId,
            @RequestParam String objectName,
            @RequestParam ObjectType objectType,
            @RequestParam Long size,
            @RequestParam String parentPath
    ) {
        return blobObjectService.saveObject(containerId, objectName, objectType, size, parentPath);
    }

    @GetMapping("/direct-children")
    public ResponseEntity<?> get(
            @RequestParam String containerId,
            @RequestParam String parentPath
    ) {
        return blobObjectService.getAllObjectsBelongingToParentPath(containerId, parentPath);
    }

    @GetMapping("/all-children")
    public ResponseEntity<?> get2(
            @RequestParam String containerId,
            @RequestParam String objectId
    ){
        return blobObjectService.getAllChildObjectsBelongingToObjectId(containerId,objectId);
    }

}
