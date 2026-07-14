package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prop_building")
public class PropBuilding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String buildingName;

    @Column(nullable = false, unique = true, length = 20)
    private String buildingCode;

    private Integer floors = 6;

    @Column(name = "building_type", length = 10)
    private String buildingType = "住宅";

    @Column(length = 200)
    private String address;

    @Column(length = 200)
    private String description;

    private Integer sortOrder = 0;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
