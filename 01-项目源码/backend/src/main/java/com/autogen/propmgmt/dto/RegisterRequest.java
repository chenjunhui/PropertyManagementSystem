package com.autogen.propmgmt.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String ownerNo;
    private String gender;
    private String phone;
    private String email;
}
