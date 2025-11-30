package com.blobstore.nox.metadata_module.repositories;

import com.blobstore.nox.metadata_module.models.BlobObject;
import com.blobstore.nox.metadata_module.models.Container;
import com.blobstore.nox.metadata_module.models.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlobObjectRepository extends JpaRepository<BlobObject, String> {
    Optional<BlobObject> findByParentContainerAndObjectNameAndParentPathAndObjectType(Container parentContainer, String objectName, String parentPath, ObjectType objectType);
}
