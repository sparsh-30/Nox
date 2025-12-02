package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.metadata_module.services.MainContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/container")
public class ContainerManagementController {

    private final MainContainerService mainContainerService;

    @GetMapping("/create")
    public ResponseEntity<?> createContainer(
            @RequestParam String containerName
    ) {
        return ResponseEntity.ok(mainContainerService.createContainerUsingContainerName(containerName));
    }

    @PatchMapping("/update-name")
    public ResponseEntity<?> updateContainerName(
            @RequestParam String containerId,
            @RequestParam String updatedName
    ) {
        return ResponseEntity.ok(mainContainerService.updateNameOfContainer(containerId, updatedName));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContainer(
            @RequestParam String containerId
    ) {
        return ResponseEntity.ok(mainContainerService.deleteContainer(containerId));
    }


}
