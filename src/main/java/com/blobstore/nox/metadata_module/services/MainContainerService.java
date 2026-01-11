package com.blobstore.nox.metadata_module.services;

import com.blobstore.nox.auth_module.services.UserAuthHelperService;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.blobstore.nox.content_module.services.ContentManagementHelperService;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.utils.EntitiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainContainerService {

    private final ContainerHelperService containerHelperService;
    private final ContentManagementHelperService contentManagementHelperService;
    private final EntitiesMapper mapper;
    private final UserAuthHelperService userAuthHelperService;

    public List<Container> getAll() {
        return containerHelperService.getAll();
    }

    public List<Container> getContainersByUserId(String userId) {
//        System.out.println("Received userId is: " + userId);
        if (!userAuthHelperService.doesUserExistByUserId(userId)) {
            throw new CustomAppException("UserId is not valid", ResponseCode.AUTH_USER_NOT_FOUND);
        }
        return containerHelperService.getAllContainersByUserId(userId);
    }

    public Map<String, Object> createContainerUsingContainerName(String userId, String containerName) {
        Container createdContainer = containerHelperService.createNewContainer(userId, containerName);
        return mapper.mapContainerToMap(createdContainer);
    }

    public Map<String, Object> updateNameOfContainer(String containerId, String updatedName) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        Container containerWithUpdatedName = containerHelperService.updateContainerName(associatedContainer, updatedName);
        return mapper.mapContainerToMap(containerWithUpdatedName);
    }

    public void deleteContainer(String containerId) {
        Container associatedContainer = containerHelperService.validateContainerId(containerId);
        contentManagementHelperService.emptyAllChildrenOfParentPath(containerId, "/");
        containerHelperService.deleteContainer(associatedContainer);
    }

}
