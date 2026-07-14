package com.autogen.propmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_role_menu")
public class SysRoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roleId;

    private Long menuId;
}
