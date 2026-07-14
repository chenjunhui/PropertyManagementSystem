package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.InspectionRecord;
import com.autogen.propmgmt.service.InspectionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspection-records")
@RequiredArgsConstructor
public class InspectionRecordController {

    private final InspectionRecordService hygieneinspectionservice;

    @GetMapping
    public Result<List<InspectionRecord>> list() {
        return Result.ok(hygieneinspectionservice.list());
    }

    @GetMapping("/{id}")
    public Result<InspectionRecord> get(@PathVariable Long id) {
        return Result.ok(hygieneinspectionservice.getById(id));
    }

    @PostMapping
    public Result<InspectionRecord> create(@RequestBody InspectionRecord body) {
        return Result.ok(hygieneinspectionservice.save(body));
    }

    @PutMapping("/{id}")
    public Result<InspectionRecord> update(@PathVariable Long id, @RequestBody InspectionRecord body) {
        body.setId(id);
        return Result.ok(hygieneinspectionservice.save(body));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        hygieneinspectionservice.delete(id);
        return Result.ok();
    }

}
