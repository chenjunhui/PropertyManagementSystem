package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "repair_request")
public class RepairRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(length = 20)
    private String status = "PENDING";

    private LocalDate submitDate;

    private LocalDate finishDate;

    @Column(length = 200)
    private String remark;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (submitDate == null) {
            submitDate = LocalDate.now();
        }
    }
}
