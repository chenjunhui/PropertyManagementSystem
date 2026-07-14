package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.VisitorRecord;
import com.autogen.propmgmt.service.VisitorRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "访客管理", description = "访客记录的增删改查")
@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorRecordController {

    private final VisitorRecordService visitorrecordservice;

    @Operation(summary = "获取访客列表")
    @GetMapping
    public Result<List<VisitorRecord>> list() {
        return Result.ok(visitorrecordservice.list());
    }

    @Operation(summary = "获取访客详情")
    @GetMapping("/{id}")
    public Result<VisitorRecord> get(@PathVariable Long id) {
        return Result.ok(visitorrecordservice.getById(id));
    }

    @Operation(summary = "新增访客记录")
    @PostMapping
    public Result<VisitorRecord> create(@RequestBody VisitorRecord body) {
        return Result.ok(visitorrecordservice.save(body));
    }

    @Operation(summary = "更新访客记录")
    @PutMapping("/{id}")
    public Result<VisitorRecord> update(@PathVariable Long id, @RequestBody VisitorRecord body) {
        body.setId(id);
        return Result.ok(visitorrecordservice.save(body));
    }

    @Operation(summary = "删除访客记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        visitorrecordservice.delete(id);
        return Result.ok();
    }

}
