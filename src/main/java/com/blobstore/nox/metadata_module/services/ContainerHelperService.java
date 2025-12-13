package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.repositories.ContainerRepository;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContainerHelperService {

    private final ContainerRepository containerRepository;
    private final EntitiesMapper mapper;

    public Container getContainerFromContainerId(String containerId) {
        Optional<Container> container = containerRepository.findByContainerId(containerId);
        return container.orElse(null);
    }

    public String getTargetContainerId(Container targetContainer) {
        return targetContainer.getContainerId();
    }

    public Container validateContainerId(String containerId) {
        Container container = getContainerFromContainerId(containerId);
        if (container == null) {
            throw new CustomAppException("ContainerId is not valid", ResponseCode.INVALID_CONTAINER_ID);
        }
        return container;
    }

    public Container saveContainer(Container container) {
        return containerRepository.save(container);
    }

    public Container updateContainerName(Container container, String updatedName) {
        container.setContainerName(updatedName);
        return saveContainer(container);
    }

    public Container createNewContainer(String containerName) {
        Container tempContainer = mapper.createContainerEntity(containerName, UUIDGenerator.generateUUIDString());
        return saveContainer(tempContainer);
    }

    public void deleteContainer(Container container) {
        containerRepository.delete(container);
    }


}
