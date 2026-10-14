package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.ObjectType;
import com.blobstore.nox.metadata_module.repositories.BlobObjectRepository;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlobObjectHelperServiceTest {

    @Mock
    private BlobObjectRepository blobObjectRepository;
    @Mock
    private EntitiesMapper mapper;

    @InjectMocks
    private BlobObjectHelperService blobObjectHelperService;

    @Test
    void validateObjectId_throwsWhenNotFound() {
        when(blobObjectRepository.findByObjectId("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> blobObjectHelperService.validateObjectId("x"))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.INVALID_OBJECT_ID);
    }

    @Test
    void generateParentPathFromObject_appendsTrailingSlash() {
        BlobObject folder = BlobObject.builder()
                .parentPath("/")
                .objectName("docs")
                .build();

        assertThat(blobObjectHelperService.generateParentPathFromObject(folder)).isEqualTo("/docs/");
    }

    @Test
    void getAllChildrenByObjectId_returnsEmptyForFile() {
        BlobObject file = BlobObject.builder()
                .objectId("f1")
                .objectType(ObjectType.FILE)
                .build();
        when(blobObjectRepository.findByObjectId("f1")).thenReturn(Optional.of(file));

        assertThat(blobObjectHelperService.getAllChildrenByObjectId("c1", "f1")).isEmpty();
    }

    @Test
    void validatePresenceOfSimilarObject_throwsWhenDuplicateExists() {
        when(blobObjectRepository.findByParentContainerIdAndObjectNameAndParentPathAndObjectType(
                "c1", "a.txt", "/", ObjectType.FILE
        )).thenReturn(Optional.of(new BlobObject()));

        assertThatThrownBy(() ->
                blobObjectHelperService.validatePresenceOfSimilarObject("c1", "a.txt", "/", ObjectType.FILE))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.OBJECT_ALREADY_EXISTS);
    }

    @Test
    void getListOfDirectChildOfParentPath_mapsEntities() {
        BlobObject child = BlobObject.builder().objectId("c1").objectName("a.txt").build();
        when(blobObjectRepository.findByParentContainerIdAndParentPath("container", "/"))
                .thenReturn(List.of(child));
        when(mapper.mapBlobObjectToDTO(child)).thenReturn(
                new com.blobstore.nox.metadata_module.dtos.ReturnBlobObject(
                        "c1", "a.txt", ObjectType.FILE, 1L, "/", null, "container"
                )
        );

        assertThat(blobObjectHelperService.getListOfDirectChildOfParentPath("container", "/"))
                .hasSize(1)
                .first()
                .extracting(com.blobstore.nox.metadata_module.dtos.ReturnBlobObject::objectName)
                .isEqualTo("a.txt");
    }

    @Test
    void deleteBlobObject_delegatesToRepository() {
        blobObjectHelperService.deleteBlobObject("obj-1");
        verify(blobObjectRepository).deleteById("obj-1");
    }
}
