package com.fuji.airbnb_clone_backend.user.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "authority")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Authority implements Serializable {

    @Id
    @Column(length = 50)
    @NotNull
    @Size(max = 50)
    private String name;
}
