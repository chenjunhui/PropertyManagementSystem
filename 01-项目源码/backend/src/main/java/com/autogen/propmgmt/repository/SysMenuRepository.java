package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {
    List<SysMenu> findByPortalAndStatusOrderBySortOrderAsc(String portal, Integer status);
}
