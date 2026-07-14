package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.dto.LoginRequest;
import com.autogen.propmgmt.dto.LoginResponse;
import com.autogen.propmgmt.dto.RefreshTokenRequest;
import com.autogen.propmgmt.dto.RegisterRequest;
import com.autogen.propmgmt.dto.TokenResponse;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.service.AuthService;
import com.autogen.propmgmt.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "用户登录、注册、Token刷新")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterService registerService;

    @Operation(summary = "用户登录", description = "支持admin和client两种门户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request,
                                       @RequestParam(defaultValue = "admin") String portal) {
        return Result.ok(authService.login(request, portal));
    }

    @Operation(summary = "业主注册", description = "新业主注册账号")
    @PostMapping("/register")
    public Result<Owner> register(@RequestBody RegisterRequest request) {
        return Result.ok(registerService.registerOwner(request));
    }

    @Operation(summary = "刷新Token", description = "使用RefreshToken获取新的AccessToken")
    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return Result.ok(authService.refreshToken(request.getRefreshToken()));
    }
}
