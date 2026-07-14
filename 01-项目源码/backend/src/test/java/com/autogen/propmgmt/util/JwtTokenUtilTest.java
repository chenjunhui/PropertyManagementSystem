package com.autogen.propmgmt.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "testSecretKey123456789012345678901234567890");
    }

    @Test
    void generateAccessToken() {
        String token = jwtTokenUtil.generateAccessToken(1L, "admin", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateRefreshToken() {
        String token = jwtTokenUtil.generateRefreshToken(1L, "admin");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken() {
        String token = jwtTokenUtil.generateAccessToken(1L, "admin", "ADMIN");

        assertTrue(jwtTokenUtil.validateToken(token));
    }

    @Test
    void getUsernameFromToken() {
        String token = jwtTokenUtil.generateAccessToken(1L, "admin", "ADMIN");

        String username = jwtTokenUtil.getUsernameFromToken(token);

        assertEquals("admin", username);
    }

    @Test
    void getUserIdFromToken() {
        String token = jwtTokenUtil.generateAccessToken(1L, "admin", "ADMIN");

        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        assertEquals(1L, userId);
    }

    @Test
    void getRoleFromToken() {
        String token = jwtTokenUtil.generateAccessToken(1L, "admin", "ADMIN");

        String role = jwtTokenUtil.getRoleFromToken(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void isRefreshToken() {
        String refreshToken = jwtTokenUtil.generateRefreshToken(1L, "admin");
        String accessToken = jwtTokenUtil.generateAccessToken(1L, "admin", "ADMIN");

        assertTrue(jwtTokenUtil.isRefreshToken(refreshToken));
        assertFalse(jwtTokenUtil.isRefreshToken(accessToken));
    }

    @Test
    void invalidToken() {
        assertFalse(jwtTokenUtil.validateToken("invalid_token"));
    }
}
