package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "visitor_record")
public class VisitorRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 50)
    private String visitorName;

    @Column(length = 20)
    private String visitorPhone;

    @Column
    private LocalDate visitDate;

    @Column(length = 200)
    private String reason;

    @Column(length = 20)
    private String status = "PENDING";

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
