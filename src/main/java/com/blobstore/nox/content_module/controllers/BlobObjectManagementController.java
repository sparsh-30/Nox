package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.MainBlobObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/object")
public class BlobObjectManagementController {

    private final MainBlobObjectService mainBlobObjectService;

    @PostMapping("/create/folder")
    public ResponseEntity<?> createObject(
            @RequestParam String containerId,
            @RequestParam String objectName,
            @RequestParam Long size,
            @RequestParam String parentPath
    ) {
        return ResponseEntity.ok(mainBlobObjectService.saveObject(containerId, objectName, ObjectType.FOLDER, size, parentPath));
    }

    @GetMapping("/get/children")
    public ResponseEntity<?> get(
            @RequestParam String containerId,
            @RequestParam String parentPath
    ) {
        return ResponseEntity.ok(mainBlobObjectService.getAllObjectsBelongingToParentPath(containerId, parentPath));
    }

}
