package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropUnit;
import com.autogen.propmgmt.service.PropUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class PropUnitController {

    private final PropUnitService unitService;

    @GetMapping
    public Result<List<PropUnit>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long buildingId) {
        return Result.ok(unitService.list(keyword, buildingId));
    }

    @GetMapping("/{id}")
    public Result<PropUnit> get(@PathVariable Long id) {
        return Result.ok(unitService.getById(id));
    }

    @PostMapping
    public Result<PropUnit> create(@RequestBody PropUnit unit) {
        return Result.ok(unitService.save(unit));
    }

    @PutMapping("/{id}")
    public Result<PropUnit> update(@PathVariable Long id, @RequestBody PropUnit unit) {
        unit.setId(id);
        return Result.ok(unitService.save(unit));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        unitService.delete(id);
        return Result.ok();
    }
}
