package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.VisitorRecord;
import com.autogen.propmgmt.service.VisitorRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorRecordController {

    private final VisitorRecordService visitorrecordservice;

    @GetMapping
    public Result<List<VisitorRecord>> list() {
        return Result.ok(visitorrecordservice.list());
    }

    @GetMapping("/{id}")
    public Result<VisitorRecord> get(@PathVariable Long id) {
        return Result.ok(visitorrecordservice.getById(id));
    }

    @PostMapping
    public Result<VisitorRecord> create(@RequestBody VisitorRecord body) {
        return Result.ok(visitorrecordservice.save(body));
    }

    @PutMapping("/{id}")
    public Result<VisitorRecord> update(@PathVariable Long id, @RequestBody VisitorRecord body) {
        body.setId(id);
        return Result.ok(visitorrecordservice.save(body));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        visitorrecordservice.delete(id);
        return Result.ok();
    }

}
