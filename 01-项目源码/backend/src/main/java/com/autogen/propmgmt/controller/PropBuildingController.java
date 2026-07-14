package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropBuilding;
import com.autogen.propmgmt.service.PropBuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "楼栋管理", description = "楼栋信息的增删改查")
@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class PropBuildingController {

    private final PropBuildingService buildingService;

    @Operation(summary = "获取楼栋列表")
    @GetMapping
    public Result<List<PropBuilding>> list(@RequestParam(required = false) String keyword) {
        return Result.ok(buildingService.list(keyword));
    }

    @Operation(summary = "获取楼栋详情")
    @GetMapping("/{id}")
    public Result<PropBuilding> get(@PathVariable Long id) {
        return Result.ok(buildingService.getById(id));
    }

    @Operation(summary = "新增楼栋")
    @PostMapping
    public Result<PropBuilding> create(@RequestBody PropBuilding building) {
        return Result.ok(buildingService.save(building));
    }

    @Operation(summary = "更新楼栋")
    @PutMapping("/{id}")
    public Result<PropBuilding> update(@PathVariable Long id, @RequestBody PropBuilding building) {
        building.setId(id);
        return Result.ok(buildingService.save(building));
    }

    @Operation(summary = "删除楼栋")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.delete(id);
        return Result.ok();
    }
}
