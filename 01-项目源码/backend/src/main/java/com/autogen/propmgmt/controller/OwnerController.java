package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping
    public Result<List<Owner>> list(@RequestParam(required = false) String keyword) {
        return Result.ok(ownerService.list(keyword));
    }

    @GetMapping("/{id}")
    public Result<Owner> get(@PathVariable Long id) {
        return Result.ok(ownerService.getById(id));
    }

    @GetMapping("/me")
    public Result<Owner> me(@RequestParam Long userId) {
        return Result.ok(ownerService.getByUserId(userId));
    }

    @PostMapping
    public Result<Owner> create(@RequestBody Owner owner) {
        return Result.ok(ownerService.save(owner));
    }

    @PutMapping("/{id}")
    public Result<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        owner.setId(id);
        return Result.ok(ownerService.save(owner));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        ownerService.delete(id);
        return Result.ok();
    }
}
