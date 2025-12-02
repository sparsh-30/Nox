package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.metadata_module.models.Container;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainContainerService {

    private final ContainerHelperService containerHelperService;

    public String createContainerUsingContainerName(String containerName) {
        Container createdContainer = containerHelperService.createNewContainer(containerName);
        return createdContainer.getContainerId();
    }

    public String updateNameOfContainer(String containerId, String updatedName) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        containerHelperService.updateContainerName(associatedContainer, updatedName);
        return "Name Updated!!";
    }

    public String deleteContainer(String containerId) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        containerHelperService.deleteContainer(associatedContainer);
        return "Container deleted!!";
    }

}
