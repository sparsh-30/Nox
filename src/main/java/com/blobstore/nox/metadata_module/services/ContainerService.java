package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.common.UUIDGenerator;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.exception_handling.ErrorCode;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.repositories.ContainerRepository;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final EntitiesMapper mapper;

    public String createContainerUsingContainerName(String containerName) {
        // Generate a container entity using passed fields
        Container tempContainer = mapper.createContainerEntity(containerName, UUIDGenerator.generateUUIDString());

        // Make a save request to database to create container entry
        Container savedContainer = containerRepository.save(tempContainer);

        return savedContainer.getContainerId();
    }

    public String updateNameOfContainer(String containerId, String updatedName) {
        // Fetch the associated container with the provided containerId
        Optional<Container> associatedContainerWithId = containerRepository.findByContainerId(containerId);

        // Check if the container exists or not
        if (associatedContainerWithId.isEmpty()) {
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Create a container entity with updated name
        Container containerWithUpdatedName = associatedContainerWithId.get();
        containerWithUpdatedName.setContainerName(updatedName);

        // Make update request to database to save the new entity
        containerRepository.save(containerWithUpdatedName);

        return "Name Updated!!";
    }

    public String deleteContainer(String containerId) {
        // Fetch the associated container with the provided containerId
        Optional<Container> associatedContainerWithId = containerRepository.findByContainerId(containerId);

        // Check if the container exists or not
        if (associatedContainerWithId.isEmpty()) {
            throw new CustomAppException("ContainerId is not valid", ErrorCode.INVALID_CONTAINER_ID);
        }

        // Make a delete request to database to delete the container entity
        containerRepository.delete(associatedContainerWithId.get());

        return "Container deleted!!";
    }

}
