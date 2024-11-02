package com.fuji.airbnb_clone_backend.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "authority")
@Data
@Builder
public class Authority implements Serializable {

    @Id
    @Column(length = 50)
    @NotNull
    @Size(max = 50)
    private String id;
}
