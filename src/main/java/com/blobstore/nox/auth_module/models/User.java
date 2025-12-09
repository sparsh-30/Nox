package com.blobstore.nox.auth_module.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "users")
public class User {
    @Id
    private String userId;
    private String username;
    private String password;

    @CreationTimestamp
    private LocalDateTime userCreatedAt;
}
