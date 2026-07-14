package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropBuilding;
import com.autogen.propmgmt.service.PropBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class PropBuildingController {

    private final PropBuildingService buildingService;

    @GetMapping
    public Result<List<PropBuilding>> list(@RequestParam(required = false) String keyword) {
        return Result.ok(buildingService.list(keyword));
    }

    @GetMapping("/{id}")
    public Result<PropBuilding> get(@PathVariable Long id) {
        return Result.ok(buildingService.getById(id));
    }

    @PostMapping
    public Result<PropBuilding> create(@RequestBody PropBuilding building) {
        return Result.ok(buildingService.save(building));
    }

    @PutMapping("/{id}")
    public Result<PropBuilding> update(@PathVariable Long id, @RequestBody PropBuilding building) {
        building.setId(id);
        return Result.ok(buildingService.save(building));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.delete(id);
        return Result.ok();
    }
}
