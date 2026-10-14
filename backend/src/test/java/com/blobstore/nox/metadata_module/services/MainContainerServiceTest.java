package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.auth_module.services.UserAuthHelperService;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.content_module.services.ContentManagementHelperService;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainContainerServiceTest {

    @Mock
    private ContainerHelperService containerHelperService;
    @Mock
    private ContentManagementHelperService contentManagementHelperService;
    @Mock
    private EntitiesMapper mapper;
    @Mock
    private UserAuthHelperService userAuthHelperService;

    @InjectMocks
    private MainContainerService mainContainerService;

    @Test
    void getContainersByUserId_throwsWhenUserInvalid() {
        when(userAuthHelperService.doesUserExistByUserId("bad")).thenReturn(false);

        assertThatThrownBy(() -> mainContainerService.getContainersByUserId("bad"))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.AUTH_USER_NOT_FOUND);
    }

    @Test
    void getContainersByUserId_returnsContainersForUser() {
        Container c = Container.builder().containerId("c1").userId("u1").build();
        when(userAuthHelperService.doesUserExistByUserId("u1")).thenReturn(true);
        when(containerHelperService.getAllContainersByUserId("u1")).thenReturn(List.of(c));

        assertThat(mainContainerService.getContainersByUserId("u1")).containsExactly(c);
    }

    @Test
    void deleteContainer_emptiesChildrenThenDeletesContainer() {
        Container container = Container.builder().containerId("c1").build();
        when(containerHelperService.validateContainerId("c1")).thenReturn(container);

        mainContainerService.deleteContainer("c1");

        verify(contentManagementHelperService).emptyAllChildrenOfParentPath("c1", "/");
        verify(containerHelperService).deleteContainer(container);
    }

    @Test
    void createContainerUsingContainerName_mapsCreatedContainer() {
        Container created = Container.builder().containerId("c1").containerName("docs").userId("u1").build();
        Map<String, Object> mapped = Map.of("containerId", "c1");
        when(containerHelperService.createNewContainer("u1", "docs")).thenReturn(created);
        when(mapper.mapContainerToMap(created)).thenReturn(mapped);

        assertThat(mainContainerService.createContainerUsingContainerName("u1", "docs")).isEqualTo(mapped);
    }
}
