package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_role")
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String roleCode;

    @Column(nullable = false, length = 50)
    private String roleName;

    @Column(length = 200)
    private String description;

    private Integer status = 1;
}
