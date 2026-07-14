package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "业主管理", description = "业主信息的增删改查")
@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @Operation(summary = "获取业主列表", description = "支持按姓名关键字搜索")
    @GetMapping
    public Result<List<Owner>> list(@RequestParam(required = false) String keyword) {
        return Result.ok(ownerService.list(keyword));
    }

    @Operation(summary = "获取业主详情", description = "根据ID获取业主信息")
    @GetMapping("/{id}")
    public Result<Owner> get(@PathVariable Long id) {
        return Result.ok(ownerService.getById(id));
    }

    @Operation(summary = "获取当前用户业主信息", description = "根据用户ID获取对应的业主信息")
    @GetMapping("/me")
    public Result<Owner> me(@RequestParam Long userId) {
        return Result.ok(ownerService.getByUserId(userId));
    }

    @Operation(summary = "新增业主", description = "创建新的业主记录")
    @PostMapping
    public Result<Owner> create(@RequestBody Owner owner) {
        return Result.ok(ownerService.save(owner));
    }

    @Operation(summary = "更新业主", description = "修改业主信息")
    @PutMapping("/{id}")
    public Result<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        owner.setId(id);
        return Result.ok(ownerService.save(owner));
    }

    @Operation(summary = "删除业主", description = "删除指定业主记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        ownerService.delete(id);
        return Result.ok();
    }
}
