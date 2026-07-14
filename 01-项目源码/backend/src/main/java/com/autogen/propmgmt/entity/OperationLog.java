package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_operation_log")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(nullable = false, length = 100)
    private String operation;

    @Column(length = 50)
    private String operator;

    @Column(length = 20)
    private String operatorRole;

    @Column(length = 255)
    private String detail;

    @Column(length = 50)
    private String ip;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(length = 500)
    private String errorMsg;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
