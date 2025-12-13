package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.common.response_handling.ApiResponse;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.services.MainContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/container")
public class ContainerManagementController {

    private final MainContainerService mainContainerService;

    @GetMapping("/create")
    public ResponseEntity<?> createContainer(
            @RequestParam String containerName
    ) {
        Map<String, Object> res = mainContainerService.createContainerUsingContainerName(containerName);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "Container is created successfully", res));
    }

    @PatchMapping("/update-name")
    public ResponseEntity<?> updateContainerName(
            @RequestParam String containerId,
            @RequestParam String updatedName
    ) {
        Map<String, Object> res = mainContainerService.updateNameOfContainer(containerId, updatedName);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "Container name is updated successfully", res));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContainer(
            @RequestParam String containerId
    ) {
        mainContainerService.deleteContainer(containerId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "Container is deleted successfully", null));
    }


}
