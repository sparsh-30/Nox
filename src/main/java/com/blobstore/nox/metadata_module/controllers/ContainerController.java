package com.blobstore.nox.metadata_module.controllers;

import com.blobstore.nox.metadata_module.services.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/container")
public class ContainerController {

    private final ContainerService containerService;

    @GetMapping("/create")
    public ResponseEntity<?> createContainer(
            @RequestParam String containerName
    ) {
        return containerService.createContainerUsingContainerName(containerName);
    }

    @PatchMapping("/update-name")
    public ResponseEntity<?> updateContainerName(
            @RequestParam String containerId,
            @RequestParam String updatedName
    ) {
        return containerService.updateNameOfContainer(containerId, updatedName);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContainer(
            @RequestParam String containerId
    ) {
        return containerService.deleteContainer(containerId);
    }

}
