package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.RepairRequest;
import com.autogen.propmgmt.service.RepairRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "报修管理", description = "报修工单的创建、查询、状态更新")
@RestController
@RequestMapping("/api/repairs")
@RequiredArgsConstructor
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    @Operation(summary = "获取报修列表", description = "支持按状态筛选")
    @GetMapping
    public Result<List<RepairRequest>> list(@RequestParam(required = false) String status) {
        return Result.ok(repairRequestService.list(status));
    }

    @Operation(summary = "获取业主报修列表", description = "根据业主ID获取报修记录")
    @GetMapping("/owner/{ownerId}")
    public Result<List<RepairRequest>> listByOwner(@PathVariable Long ownerId) {
        return Result.ok(repairRequestService.listByOwner(ownerId));
    }

    @Operation(summary = "获取报修详情", description = "根据ID获取报修工单详情")
    @GetMapping("/{id}")
    public Result<RepairRequest> get(@PathVariable Long id) {
        return Result.ok(repairRequestService.getById(id));
    }

    @Operation(summary = "创建报修", description = "业主提交新的报修工单")
    @PostMapping
    public Result<RepairRequest> create(@RequestBody RepairRequest request) {
        return Result.ok(repairRequestService.save(request));
    }

    @Operation(summary = "更新报修状态", description = "管理员更新报修工单状态")
    @PutMapping("/{id}/status")
    public Result<RepairRequest> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return Result.ok(repairRequestService.updateStatus(id, body.get("status"), body.get("remark")));
    }

    @Operation(summary = "删除报修", description = "删除报修工单记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        repairRequestService.delete(id);
        return Result.ok();
    }
}
