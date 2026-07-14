package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.SysMenu;
import com.autogen.propmgmt.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @GetMapping
    public Result<List<SysMenu>> list(@RequestParam(required = false) String role) {
        if (role != null && !role.isBlank()) {
            return Result.ok(menuService.listByRole(role));
        }
        return Result.ok(menuService.listAll());
    }

    @PostMapping
    public Result<SysMenu> create(@RequestBody SysMenu menu) {
        return Result.ok(menuService.save(menu));
    }

    @PutMapping("/{id}")
    public Result<SysMenu> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        return Result.ok(menuService.save(menu));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.ok();
    }
}
