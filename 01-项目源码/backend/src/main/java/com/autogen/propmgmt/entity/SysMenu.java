package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId = 0L;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 100)
    private String path;

    @Column(length = 50)
    private String icon;

    private Integer sortOrder = 0;

    @Column(nullable = false, length = 20)
    private String portal = "admin";

    private Integer status = 1;
}
