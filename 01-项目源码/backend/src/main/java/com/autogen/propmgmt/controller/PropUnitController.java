package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropUnit;
import com.autogen.propmgmt.service.PropUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房屋管理", description = "房屋信息的增删改查")
@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class PropUnitController {

    private final PropUnitService unitService;

    @Operation(summary = "获取房屋列表", description = "支持按关键字和楼栋ID筛选")
    @GetMapping
    public Result<List<PropUnit>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long buildingId) {
        return Result.ok(unitService.list(keyword, buildingId));
    }

    @Operation(summary = "获取房屋详情")
    @GetMapping("/{id}")
    public Result<PropUnit> get(@PathVariable Long id) {
        return Result.ok(unitService.getById(id));
    }

    @Operation(summary = "新增房屋")
    @PostMapping
    public Result<PropUnit> create(@RequestBody PropUnit unit) {
        return Result.ok(unitService.save(unit));
    }

    @Operation(summary = "更新房屋")
    @PutMapping("/{id}")
    public Result<PropUnit> update(@PathVariable Long id, @RequestBody PropUnit unit) {
        unit.setId(id);
        return Result.ok(unitService.save(unit));
    }

    @Operation(summary = "删除房屋")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        unitService.delete(id);
        return Result.ok();
    }
}
