package com.autogen.propmgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String username;
    private String name;
    private String role;
    private String token;
    private String refreshToken;
    private Long expiresIn;
    private Long ownerId;
    private String avatar;
}
