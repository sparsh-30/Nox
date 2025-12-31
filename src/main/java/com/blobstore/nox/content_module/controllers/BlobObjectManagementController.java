package com.blobstore.nox.content_module.controllers;

import com.blobstore.nox.common.response_handling.ApiResponse;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.MainBlobObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        Map<String, Object> res = mainBlobObjectService.saveObject(containerId, objectName, ObjectType.FOLDER, size, parentPath);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, "Object created successfully", res));
    }

    @GetMapping("/get/children")
    public ResponseEntity<?> getChildren(
            @RequestParam String containerId,
            @RequestParam String parentPath
    ) {
        List<ReturnBlobObject> res = mainBlobObjectService.getAllObjects(containerId, parentPath);
        return ResponseEntity
                .status(ResponseCode.SUCCESS.getStatus())
                .body(new ApiResponse(true, ResponseCode.SUCCESS, null, res));
    }

}
