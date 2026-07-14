package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.SysRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysRoleMenuRepository extends JpaRepository<SysRoleMenu, Long> {
    List<SysRoleMenu> findByRoleId(Long roleId);
    void deleteByRoleId(Long roleId);
}
