package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prop_unit")
public class PropUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false, length = 20)
    private String unitNo;

    private Integer floor = 1;

    @Column(length = 30)
    private String unitType;

    @Column(nullable = false)
    private BigDecimal areaSqm = BigDecimal.valueOf(80);

    private Integer ownerCount = 0;

    /** 1已入住 2空置 0装修中 */
    private Integer status = 1;

    @Column(length = 200)
    private String description;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
