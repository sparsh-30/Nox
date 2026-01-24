package com.blobstore.nox.metadata_module.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "containers")
public class Container {
    @Id
    private String containerId;
    private String containerName;
    private String userId;
    @CreationTimestamp
    private LocalDateTime containerCreatedAt;
}
