package com.fuji.airbnb_clone_backend.shared.kernel;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AbstractAuditingEntity.class)
@Data
@AllArgsConstructor @NoArgsConstructor
public abstract class AbstractAuditingEntity<T> {

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Instant createdDate= Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate= Instant.now();

    public abstract T getId();
}
