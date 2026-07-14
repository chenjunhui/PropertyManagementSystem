package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropAnnouncement;
import com.autogen.propmgmt.service.PropAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "公告管理", description = "公告通知的增删改查")
@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class PropAnnouncementController {

    private final PropAnnouncementService dormannouncementservice;

    @Operation(summary = "获取公告列表")
    @GetMapping
    public Result<List<PropAnnouncement>> list() {
        return Result.ok(dormannouncementservice.list());
    }

    @Operation(summary = "获取公告详情")
    @GetMapping("/{id}")
    public Result<PropAnnouncement> get(@PathVariable Long id) {
        return Result.ok(dormannouncementservice.getById(id));
    }

    @Operation(summary = "发布公告")
    @PostMapping
    public Result<PropAnnouncement> create(@RequestBody PropAnnouncement body) {
        return Result.ok(dormannouncementservice.save(body));
    }

    @Operation(summary = "更新公告")
    @PutMapping("/{id}")
    public Result<PropAnnouncement> update(@PathVariable Long id, @RequestBody PropAnnouncement body) {
        body.setId(id);
        return Result.ok(dormannouncementservice.save(body));
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dormannouncementservice.delete(id);
        return Result.ok();
    }

}
