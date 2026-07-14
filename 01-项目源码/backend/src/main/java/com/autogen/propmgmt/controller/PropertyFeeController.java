package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropertyFee;
import com.autogen.propmgmt.service.PropertyFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property-fees")
@RequiredArgsConstructor
public class PropertyFeeController {

    private final PropertyFeeService utilitybillservice;

    @GetMapping
    public Result<List<PropertyFee>> list() {
        return Result.ok(utilitybillservice.list());
    }

    @GetMapping("/{id}")
    public Result<PropertyFee> get(@PathVariable Long id) {
        return Result.ok(utilitybillservice.getById(id));
    }

    @PostMapping
    public Result<PropertyFee> create(@RequestBody PropertyFee body) {
        return Result.ok(utilitybillservice.save(body));
    }

    @PutMapping("/{id}")
    public Result<PropertyFee> update(@PathVariable Long id, @RequestBody PropertyFee body) {
        body.setId(id);
        return Result.ok(utilitybillservice.save(body));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        utilitybillservice.delete(id);
        return Result.ok();
    }

}
