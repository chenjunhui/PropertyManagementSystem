package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.entity.PropAnnouncement;
import com.autogen.propmgmt.service.PropAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class PropAnnouncementController {

    private final PropAnnouncementService dormannouncementservice;

    @GetMapping
    public Result<List<PropAnnouncement>> list() {
        return Result.ok(dormannouncementservice.list());
    }

    @GetMapping("/{id}")
    public Result<PropAnnouncement> get(@PathVariable Long id) {
        return Result.ok(dormannouncementservice.getById(id));
    }

    @PostMapping
    public Result<PropAnnouncement> create(@RequestBody PropAnnouncement body) {
        return Result.ok(dormannouncementservice.save(body));
    }

    @PutMapping("/{id}")
    public Result<PropAnnouncement> update(@PathVariable Long id, @RequestBody PropAnnouncement body) {
        body.setId(id);
        return Result.ok(dormannouncementservice.save(body));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dormannouncementservice.delete(id);
        return Result.ok();
    }

}
