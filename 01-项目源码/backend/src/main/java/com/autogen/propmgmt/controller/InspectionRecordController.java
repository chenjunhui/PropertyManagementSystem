package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.InspectionRecord;
import com.autogen.propmgmt.service.InspectionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "巡检管理", description = "巡检记录的增删改查")
@RestController
@RequestMapping("/api/inspection-records")
@RequiredArgsConstructor
public class InspectionRecordController {

    private final InspectionRecordService hygieneinspectionservice;

    @Operation(summary = "获取巡检列表")
    @GetMapping
    public Result<List<InspectionRecord>> list() {
        return Result.ok(hygieneinspectionservice.list());
    }

    @Operation(summary = "获取巡检详情")
    @GetMapping("/{id}")
    public Result<InspectionRecord> get(@PathVariable Long id) {
        return Result.ok(hygieneinspectionservice.getById(id));
    }

    @Operation(summary = "新增巡检记录")
    @PostMapping
    public Result<InspectionRecord> create(@RequestBody InspectionRecord body) {
        return Result.ok(hygieneinspectionservice.save(body));
    }

    @Operation(summary = "更新巡检记录")
    @PutMapping("/{id}")
    public Result<InspectionRecord> update(@PathVariable Long id, @RequestBody InspectionRecord body) {
        body.setId(id);
        return Result.ok(hygieneinspectionservice.save(body));
    }

    @Operation(summary = "删除巡检记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        hygieneinspectionservice.delete(id);
        return Result.ok();
    }

}
