package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.dto.DashboardStats;
import com.autogen.propmgmt.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<DashboardStats> stats() {
        return Result.ok(dashboardService.getStats());
    }
}
