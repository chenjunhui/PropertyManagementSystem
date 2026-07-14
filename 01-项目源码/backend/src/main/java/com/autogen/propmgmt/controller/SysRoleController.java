package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.SysMenu;
import com.autogen.propmgmt.entity.SysRole;
import com.autogen.propmgmt.service.SysMenuService;
import com.autogen.propmgmt.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;
    private final SysMenuService menuService;

    @GetMapping
    public Result<List<SysRole>> list() {
        return Result.ok(roleService.list());
    }

    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        return Result.ok(roleService.save(role));
    }

    @PutMapping("/{id}")
    public Result<SysRole> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return Result.ok(roleService.save(role));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{id}/menus")
    public Result<List<Long>> getRoleMenus(@PathVariable Long id) {
        return Result.ok(menuService.getMenuIdsByRole(id));
    }

    @PutMapping("/{id}/menus")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        menuService.assignMenus(id, body.get("menuIds"));
        return Result.ok();
    }
}
