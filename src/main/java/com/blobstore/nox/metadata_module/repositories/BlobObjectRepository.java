package com.blobstore.nox.metadata_module.repositories;

import com.blobstore.nox.metadata_module.models.BlobObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlobObjectRepository extends JpaRepository<BlobObject,String> {
}
