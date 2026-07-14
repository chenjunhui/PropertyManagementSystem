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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterService registerService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request,
                                       @RequestParam(defaultValue = "admin") String portal) {
        return Result.ok(authService.login(request, portal));
    }

    @PostMapping("/register")
    public Result<Owner> register(@RequestBody RegisterRequest request) {
        return Result.ok(registerService.registerOwner(request));
    }

    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return Result.ok(authService.refreshToken(request.getRefreshToken()));
    }
}
