package com.blobstore.nox.auth_module.repositories;

import com.blobstore.nox.auth_module.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
