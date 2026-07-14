package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropertyFee;
import com.autogen.propmgmt.service.PropertyFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "物业费管理", description = "物业费账单的增删改查")
@RestController
@RequestMapping("/api/property-fees")
@RequiredArgsConstructor
public class PropertyFeeController {

    private final PropertyFeeService utilitybillservice;

    @Operation(summary = "获取物业费列表")
    @GetMapping
    public Result<List<PropertyFee>> list() {
        return Result.ok(utilitybillservice.list());
    }

    @Operation(summary = "获取物业费详情")
    @GetMapping("/{id}")
    public Result<PropertyFee> get(@PathVariable Long id) {
        return Result.ok(utilitybillservice.getById(id));
    }

    @Operation(summary = "创建物业费账单")
    @PostMapping
    public Result<PropertyFee> create(@RequestBody PropertyFee body) {
        return Result.ok(utilitybillservice.save(body));
    }

    @Operation(summary = "更新物业费账单")
    @PutMapping("/{id}")
    public Result<PropertyFee> update(@PathVariable Long id, @RequestBody PropertyFee body) {
        body.setId(id);
        return Result.ok(utilitybillservice.save(body));
    }

    @Operation(summary = "删除物业费账单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        utilitybillservice.delete(id);
        return Result.ok();
    }

}
