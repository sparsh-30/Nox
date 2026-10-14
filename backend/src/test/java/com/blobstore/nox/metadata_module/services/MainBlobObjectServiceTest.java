package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.metadata_module.dtos.ReturnBlobObject;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainBlobObjectServiceTest {

    @Mock
    private ContainerHelperService containerHelperService;
    @Mock
    private BlobObjectHelperService blobObjectHelperService;
    @Mock
    private EntitiesMapper mapper;

    @InjectMocks
    private MainBlobObjectService mainBlobObjectService;

    @Test
    void saveObject_validatesAndCreatesBlob() {
        Container container = Container.builder().containerId("c1").build();
        BlobObject created = BlobObject.builder().objectId("o1").objectName("a.txt").build();
        Map<String, Object> mapped = Map.of("objectId", "o1");

        when(containerHelperService.validateContainerId("c1")).thenReturn(container);
        when(containerHelperService.getTargetContainerId(container)).thenReturn("c1");
        when(blobObjectHelperService.createNewBlobObject("a.txt", ObjectType.FILE, 10L, "/", "c1"))
                .thenReturn(created);
        when(mapper.MapBlobObjectToMap(created)).thenReturn(mapped);

        assertThat(mainBlobObjectService.saveObject("c1", "a.txt", ObjectType.FILE, 10L, "/"))
                .isEqualTo(mapped);

        verify(blobObjectHelperService).validatePresenceOfSimilarObject("c1", "a.txt", "/", ObjectType.FILE);
    }

    @Test
    void updateObjectName_updatesWhenValid() {
        Container container = Container.builder().containerId("c1").build();
        BlobObject blob = BlobObject.builder()
                .objectId("o1")
                .objectName("old.txt")
                .parentPath("/")
                .objectType(ObjectType.FILE)
                .build();

        when(containerHelperService.validateContainerId("c1")).thenReturn(container);
        when(containerHelperService.getTargetContainerId(container)).thenReturn("c1");
        when(blobObjectHelperService.validateObjectId("o1")).thenReturn(blob);

        assertThat(mainBlobObjectService.updateObjectName("c1", "o1", "new.txt"))
                .isEqualTo("Name updated!!");

        verify(blobObjectHelperService).validatePresenceOfSimilarObject("c1", "new.txt", "/", ObjectType.FILE);
        verify(blobObjectHelperService).updateBlobObjectName(blob, "new.txt");
    }

    @Test
    void getAllDirectObjects_returnsChildIds() {
        Container container = Container.builder().containerId("c1").build();
        ReturnBlobObject child = new ReturnBlobObject(
                "child-1", "x.txt", ObjectType.FILE, 1L, "/", null, "c1"
        );

        when(containerHelperService.validateContainerId("c1")).thenReturn(container);
        when(containerHelperService.getTargetContainerId(container)).thenReturn("c1");
        when(blobObjectHelperService.getAllChildrenOfParentPath("c1", "/docs/"))
                .thenReturn(List.of(child));

        Map<String, Object> result = mainBlobObjectService.getAllDirectObjects("c1", "/docs/");

        assertThat(result.get("childObjectIds")).isEqualTo(List.of("child-1"));
    }
}
