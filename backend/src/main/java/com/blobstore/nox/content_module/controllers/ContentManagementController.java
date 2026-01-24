package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.content_module.services.MainContentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ContentManagementController {

    private final MainContentManagementService mainContentManagementService;

    @DeleteMapping("/folder/empty")
    public ResponseEntity<?> emptyCompleteFolder(
            @RequestParam String containerId,
            @RequestParam String parentPath
    ) {
        return ResponseEntity.ok(mainContentManagementService.emptyAllChildrenOfParentPath(containerId, parentPath));
    }

    @DeleteMapping("/container/empty")
    public ResponseEntity<?> emptyCompleteContainer(
            @RequestParam String containerId
    ) {
        return ResponseEntity.ok(mainContentManagementService.emptyAllChildrenOfParentPath(containerId, "/"));
    }

    @DeleteMapping("/delete/object")
    public ResponseEntity<String> deleteObject(
            @RequestParam String containerId,
            @RequestParam String objectId
    ) {
        return ResponseEntity.ok(mainContentManagementService.deleteAnObject(containerId, objectId));
    }

}
