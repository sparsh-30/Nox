package com.blobstore.nox.content_module.services;

import com.blobstore.nox.files_module.services.FileOperationsService;
import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentManagementHelperServiceTest {

    @Mock
    private FileOperationsService fileOperationsService;
    @Mock
    private BlobObjectHelperService blobObjectHelperService;

    @InjectMocks
    private ContentManagementHelperService contentManagementHelperService;

    @Test
    void deleteAnObjectEntry_deletesFileWhenObjectIsFile() {
        ReturnBlobObject fileObject = new ReturnBlobObject(
                "obj-1", "a.txt", ObjectType.FILE, 1L, "/", LocalDateTime.now(), "c1"
        );

        contentManagementHelperService.deleteAnObjectEntry(fileObject);

        verify(fileOperationsService).deleteFileByObjectId("obj-1");
        verify(blobObjectHelperService).deleteBlobObject("obj-1");
    }

    @Test
    void emptyAllChildrenOfParentPath_deletesEachChild() {
        ReturnBlobObject child = new ReturnBlobObject(
                "child-1", "b.txt", ObjectType.FILE, 1L, "/a/", LocalDateTime.now(), "c1"
        );
        when(blobObjectHelperService.getAllChildrenOfParentPath("c1", "/a/")).thenReturn(List.of(child));

        contentManagementHelperService.emptyAllChildrenOfParentPath("c1", "/a/");

        verify(fileOperationsService).deleteFileByObjectId("child-1");
        verify(blobObjectHelperService).deleteBlobObject("child-1");
    }
}
