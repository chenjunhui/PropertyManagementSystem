package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inspection_record")
public class InspectionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Column(length = 50)
    private String inspectorName;

    @Column(nullable = false)
    private Integer score;

    @Column
    private LocalDate checkDate;

    @Column(length = 500)
    private String issues;

    @Column(length = 20)
    private String result = "PASS";

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
