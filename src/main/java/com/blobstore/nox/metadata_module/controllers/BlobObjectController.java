package com.blobstore.nox.metadata_module.controllers;

import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/object")
public class BlobObjectController {

    private final BlobObjectService blobObjectService;

    @PostMapping("/create/folder")
    public ResponseEntity<?> createObject(
            @RequestParam String containerId,
            @RequestParam String objectName,
            @RequestParam Long size,
            @RequestParam String parentPath
    ) {
        return ResponseEntity.ok(blobObjectService.saveObject(containerId, objectName, ObjectType.FOLDER, size, parentPath));
    }

    @GetMapping("/get/children")
    public ResponseEntity<?> get(
            @RequestParam String containerId,
            @RequestParam String parentPath
    ) {
        return ResponseEntity.ok(blobObjectService.getAllObjectsBelongingToParentPath(containerId, parentPath));
    }

}
