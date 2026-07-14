package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    Optional<SysRole> findByRoleCode(String roleCode);
}
