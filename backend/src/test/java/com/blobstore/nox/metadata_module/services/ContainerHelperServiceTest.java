package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.repositories.ContainerRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContainerHelperServiceTest {

    @Mock
    private ContainerRepository containerRepository;
    @Mock
    private EntitiesMapper mapper;

    @InjectMocks
    private ContainerHelperService containerHelperService;

    @Test
    void validateContainerId_throwsWhenMissing() {
        when(containerRepository.findByContainerId("bad")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> containerHelperService.validateContainerId("bad"))
                .isInstanceOf(CustomAppException.class)
                .extracting("responseCode")
                .isEqualTo(ResponseCode.INVALID_CONTAINER_ID);
    }

    @Test
    void getTargetContainerId_returnsContainerId() {
        Container container = Container.builder().containerId("c1").build();

        assertThat(containerHelperService.getTargetContainerId(container)).isEqualTo("c1");
    }

    @Test
    void createNewContainer_persistsMappedEntity() {
        Container entity = Container.builder().containerId("new-c").containerName("photos").userId("u1").build();
        when(mapper.createContainerEntity(eq("u1"), eq("photos"), anyString()))
                .thenReturn(entity);
        when(containerRepository.save(entity)).thenReturn(entity);

        Container saved = containerHelperService.createNewContainer("u1", "photos");

        assertThat(saved).isSameAs(entity);
        verify(containerRepository).save(entity);
    }

    @Test
    void getAllContainersByUserId_delegatesToRepository() {
        Container c = Container.builder().containerId("c1").userId("u1").build();
        when(containerRepository.findByUserId("u1")).thenReturn(List.of(c));

        assertThat(containerHelperService.getAllContainersByUserId("u1")).containsExactly(c);
    }
}
