package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "API索引", description = "接口列表")
@RestController
@RequestMapping("/api")
public class ApiController {

    @Operation(summary = "API列表", description = "返回所有可用接口")
    @GetMapping
    public Result<List<Map<String, String>>> index() {
        List<Map<String, String>> apis = List.of(
            endpoint("数据概览", "GET", "/api/dashboard/stats"),
            endpoint("用户登录", "POST", "/api/auth/login"),
            endpoint("用户登出", "POST", "/api/auth/logout"),
            endpoint("用户信息", "GET", "/api/profile/info"),
            endpoint("修改密码", "PUT", "/api/profile/password"),
            endpoint("楼栋列表", "GET", "/api/buildings"),
            endpoint("单元列表", "GET", "/api/units"),
            endpoint("业主列表", "GET", "/api/owners"),
            endpoint("报修列表", "GET", "/api/repairs"),
            endpoint("报修提交", "POST", "/api/repairs"),
            endpoint("缴费记录", "GET", "/api/fees"),
            endpoint("公告列表", "GET", "/api/announcements"),
            endpoint("访客记录", "GET", "/api/visitors"),
            endpoint("巡检记录", "GET", "/api/inspections"),
            endpoint("操作日志", "GET", "/api/logs"),
            endpoint("文件上传", "POST", "/api/upload"),
            endpoint("角色管理", "GET", "/api/roles"),
            endpoint("菜单管理", "GET", "/api/menus"),
            endpoint("系统监控", "GET", "/monitor/")
        );
        return Result.ok(apis);
    }

    private Map<String, String> endpoint(String name, String method, String path) {
        Map<String, String> ep = new LinkedHashMap<>();
        ep.put("name", name);
        ep.put("method", method);
        ep.put("path", path);
        return ep;
    }
}
