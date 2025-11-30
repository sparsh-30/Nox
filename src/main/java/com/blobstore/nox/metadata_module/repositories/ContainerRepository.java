package com.blobstore.nox.metadata_module.repositories;

import com.blobstore.nox.metadata_module.models.Container;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContainerRepository extends JpaRepository<Container,String> {
    Optional<Container> findByContainerId(String containerId);
}
