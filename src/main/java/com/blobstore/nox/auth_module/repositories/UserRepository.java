package com.blobstore.nox.auth_module.repositories;

import com.blobstore.nox.auth_module.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUsername(String username);
}
