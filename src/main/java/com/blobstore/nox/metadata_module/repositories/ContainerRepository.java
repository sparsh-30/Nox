package com.blobstore.nox.metadata_module.repositories;

import com.blobstore.nox.metadata_module.models.Container;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerRepository extends JpaRepository<Container,String> {
}
