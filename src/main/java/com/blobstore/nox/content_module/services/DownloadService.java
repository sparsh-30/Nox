package com.blobstore.nox.content_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DownloadService {

    private final BlobObjectHelperService blobObjectHelperService;

    @Value("${file.storage.path}")
    private String storagePath;

    public Map<String, Object> downloadFileFromObjectId(String objectId) {
        BlobObject resObject = blobObjectHelperService.validateObjectId(objectId);
        if (resObject == null) {
            throw new CustomAppException("ObjectId does not exists", ResponseCode.INVALID_OBJECT_ID);
        }
        String finalFilePath = storagePath + objectId + ".data";
        Path path = Paths.get(finalFilePath).normalize();

        if (!Files.exists(path)) {
            throw new CustomAppException("File does not exist on the backend", ResponseCode.FILE_NOT_PRESENT);
        }

        return Map.of(
                "path", path,
                "objectName", resObject.getObjectName()
        );
    }

}
