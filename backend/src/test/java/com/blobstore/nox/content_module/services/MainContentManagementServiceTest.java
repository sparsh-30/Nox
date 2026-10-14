package com.blobstore.nox.content_module.services;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.services.BlobObjectHelperService;
import com.blobstore.nox.metadata_module.services.ContainerHelperService;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainContentManagementServiceTest {

    @Mock
    private ContentManagementHelperService contentManagementHelperService;
    @Mock
    private ContainerHelperService containerHelperService;
    @Mock
    private BlobObjectHelperService blobObjectHelperService;
    @Mock
    private EntitiesMapper mapper;

    @InjectMocks
    private MainContentManagementService mainContentManagementService;

    @Test
    void emptyAllChildrenOfParentPath_validatesContainerAndEmptiesPath() {
        Container container = Container.builder().containerId("c1").build();
        when(containerHelperService.validateContainerId("c1")).thenReturn(container);
        when(containerHelperService.getTargetContainerId(container)).thenReturn("c1");

        String result = mainContentManagementService.emptyAllChildrenOfParentPath("c1", "/");

        assertThat(result).isEqualTo("Parent path is emptied!!");
        verify(contentManagementHelperService).emptyAllChildrenOfParentPath("c1", "/");
    }

    @Test
    void deleteAnObject_emptiesChildrenWhenFolderHasChildren() {
        Container container = Container.builder().containerId("c1").build();
        BlobObject folder = BlobObject.builder()
                .objectId("folder-1")
                .objectName("docs")
                .objectType(ObjectType.FOLDER)
                .parentPath("/")
                .build();
        ReturnBlobObject dto = new ReturnBlobObject(
                "folder-1", "docs", ObjectType.FOLDER, 0L, "/", LocalDateTime.now(), "c1"
        );
        ReturnBlobObject child = new ReturnBlobObject(
                "child-1", "x.txt", ObjectType.FILE, 1L, "/docs/", LocalDateTime.now(), "c1"
        );

        when(containerHelperService.validateContainerId("c1")).thenReturn(container);
        when(containerHelperService.getTargetContainerId(container)).thenReturn("c1");
        when(blobObjectHelperService.validateObjectId("folder-1")).thenReturn(folder);
        when(blobObjectHelperService.getAllChildrenByObjectId("c1", "folder-1")).thenReturn(List.of(child));
        when(blobObjectHelperService.generateParentPathFromObject(folder)).thenReturn("/docs/");
        when(mapper.mapBlobObjectToDTO(folder)).thenReturn(dto);

        String result = mainContentManagementService.deleteAnObject("c1", "folder-1");

        assertThat(result).isEqualTo("Object has been deleted!!");
        verify(contentManagementHelperService).emptyAllChildrenOfParentPath("c1", "/docs/");
        verify(contentManagementHelperService).deleteAnObjectEntry(dto);
    }
}
