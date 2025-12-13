package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainContainerService {

    private final ContainerHelperService containerHelperService;
    private final EntitiesMapper mapper;

    public Map<String, Object> createContainerUsingContainerName(String containerName) {
        Container createdContainer = containerHelperService.createNewContainer(containerName);
        return mapper.mapContainerToMap(createdContainer);
    }

    public Map<String, Object> updateNameOfContainer(String containerId, String updatedName) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        Container containerWithUpdatedName = containerHelperService.updateContainerName(associatedContainer, updatedName);
        return mapper.mapContainerToMap(containerWithUpdatedName);
    }

    public void deleteContainer(String containerId) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        containerHelperService.deleteContainer(associatedContainer);
    }

}
