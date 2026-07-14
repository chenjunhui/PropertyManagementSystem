package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.OperationLog;
import com.autogen.propmgmt.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "操作日志", description = "系统操作日志查询")
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class OperationLogController {
    private final OperationLogService logService;

    @Operation(summary = "获取操作日志", description = "支持按模块和操作员筛选，分页查询")
    @GetMapping
    public Result<Page<OperationLog>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operator) {
        if (module != null && !module.isEmpty()) {
            return Result.ok(logService.listByModule(module, PageRequest.of(page, size)));
        }
        if (operator != null && !operator.isEmpty()) {
            return Result.ok(logService.listByOperator(operator, PageRequest.of(page, size)));
        }
        return Result.ok(logService.list(PageRequest.of(page, size)));
    }
}
