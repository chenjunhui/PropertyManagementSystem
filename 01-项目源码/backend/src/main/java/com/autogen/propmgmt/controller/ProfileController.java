package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.dto.ProfileUpdateRequest;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.entity.User;
import com.autogen.propmgmt.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/user/{userId}")
    public Result<User> getUser(@PathVariable Long userId) {
        return Result.ok(profileService.getUserProfile(userId));
    }

    @PutMapping("/user/{userId}")
    public Result<User> updateUser(@PathVariable Long userId, @RequestBody ProfileUpdateRequest req) {
        return Result.ok(profileService.updateUserProfile(userId, req));
    }

    @PutMapping("/owner/{ownerId}")
    public Result<Owner> updateOwner(@PathVariable Long ownerId, @RequestBody ProfileUpdateRequest req) {
        return Result.ok(profileService.updateOwnerProfile(ownerId, req));
    }
}
