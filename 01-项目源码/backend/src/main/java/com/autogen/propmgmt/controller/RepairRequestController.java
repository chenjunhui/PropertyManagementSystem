package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.RepairRequest;
import com.autogen.propmgmt.service.RepairRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repairs")
@RequiredArgsConstructor
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    @GetMapping
    public Result<List<RepairRequest>> list(@RequestParam(required = false) String status) {
        return Result.ok(repairRequestService.list(status));
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<RepairRequest>> listByOwner(@PathVariable Long ownerId) {
        return Result.ok(repairRequestService.listByOwner(ownerId));
    }

    @GetMapping("/{id}")
    public Result<RepairRequest> get(@PathVariable Long id) {
        return Result.ok(repairRequestService.getById(id));
    }

    @PostMapping
    public Result<RepairRequest> create(@RequestBody RepairRequest request) {
        return Result.ok(repairRequestService.save(request));
    }

    @PutMapping("/{id}/status")
    public Result<RepairRequest> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return Result.ok(repairRequestService.updateStatus(id, body.get("status"), body.get("remark")));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        repairRequestService.delete(id);
        return Result.ok();
    }
}
