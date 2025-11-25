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
@ToString
@Table(name = "containers")
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String containerId;
    private String containerName;
    @CreationTimestamp
    private LocalDateTime containerCreatedAt;

    @OneToMany(mappedBy = "parentContainer")
    private List<BlobObject> containerObjects;
}
