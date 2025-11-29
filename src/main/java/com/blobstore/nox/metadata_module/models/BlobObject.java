package com.blobstore.nox.metadata_module.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "blob_objects")
public class BlobObject {
    @Id
    private String objectId;
    private String objectName;
    private ObjectType objectType;
    private Long size;
    private String parentPath;
    @CreationTimestamp
    private LocalDateTime objectCreatedAt;

    @ManyToOne
    @JoinColumn(name = "containerId")
    private Container parentContainer;

}
