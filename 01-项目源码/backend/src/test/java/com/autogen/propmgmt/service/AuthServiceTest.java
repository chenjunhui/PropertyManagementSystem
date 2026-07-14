package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.dto.LoginRequest;
import com.autogen.propmgmt.dto.LoginResponse;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.entity.User;
import com.autogen.propmgmt.repository.OwnerRepository;
import com.autogen.propmgmt.repository.UserRepository;
import com.autogen.propmgmt.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private MessageSource messageSource;

    @Mock
    private OperationLogService logService;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Owner testOwner;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("admin");
        testUser.setPassword("encoded_password");
        testUser.setRole("ADMIN");
        testUser.setName("管理员");
        testUser.setStatus(1);

        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setUserId(1L);
    }

    @Test
    void loginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenUtil.generateAccessToken(any(), anyString(), anyString())).thenReturn("access_token");
        when(jwtTokenUtil.generateRefreshToken(any(), anyString())).thenReturn("refresh_token");
        when(ownerRepository.findByUserId(1L)).thenReturn(Optional.of(testOwner));

        LoginResponse response = authService.login(request, "admin");

        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        assertEquals("access_token", response.getToken());
    }

    @Test
    void loginWithWrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong_password");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("用户名或密码错误");

        assertThrows(BusinessException.class, () -> authService.login(request, "admin"));
    }

    @Test
    void loginWithNonExistentUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("用户名或密码错误");

        assertThrows(BusinessException.class, () -> authService.login(request, "admin"));
    }

    @Test
    void loginWithDisabledUser() {
        testUser.setStatus(0);
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("账号已禁用");

        assertThrows(BusinessException.class, () -> authService.login(request, "admin"));
    }
}
