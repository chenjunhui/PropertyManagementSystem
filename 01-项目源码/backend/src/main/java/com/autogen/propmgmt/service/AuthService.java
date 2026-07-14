package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.dto.LoginRequest;
import com.autogen.propmgmt.dto.LoginResponse;
import com.autogen.propmgmt.dto.TokenResponse;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.entity.User;
import com.autogen.propmgmt.repository.OwnerRepository;
import com.autogen.propmgmt.repository.UserRepository;
import com.autogen.propmgmt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Set<String> ADMIN_PORTAL_ROLES = Set.of("ADMIN", "PROPERTY_MANAGER");
    private static final Set<String> CLIENT_PORTAL_ROLES = Set.of("OWNER");
    private static final long ACCESS_TOKEN_EXPIRES_IN = 1800L; // 30 minutes in seconds

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MessageSource messageSource;
    private final OperationLogService logService;

    private String msg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public LoginResponse login(LoginRequest request, String portal) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logService.logError("认证", "登录", request.getUsername(), portal, "用户不存在", null, "用户不存在");
                    return new BusinessException(msg("auth.login.failed"));
                });
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logService.logError("认证", "登录", request.getUsername(), portal, "密码错误", null, "密码错误");
            throw new BusinessException(msg("auth.login.failed"));
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            logService.logError("认证", "登录", request.getUsername(), portal, "账号已禁用", null, "账号已禁用");
            throw new BusinessException(msg("auth.login.disabled"));
        }
        validatePortal(user.getRole(), portal);
        Long ownerId = ownerRepository.findByUserId(user.getId())
                .map(Owner::getId)
                .orElse(null);
        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getUsername());
        String avatar = resolveAvatar(user);
        logService.log("认证", "登录", user.getUsername(), user.getRole(), "登录成功", null);
        return new LoginResponse(user.getId(), user.getUsername(), user.getName(),
                user.getRole(), accessToken, refreshToken, ACCESS_TOKEN_EXPIRES_IN, ownerId, avatar);
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenUtil.validateToken(refreshToken) || !jwtTokenUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(msg("jwt.expired"));
        }
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(msg("auth.refresh.user_not_found")));
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(msg("auth.refresh.disabled"));
        }
        String newAccessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getUsername());
        return new TokenResponse(newAccessToken, newRefreshToken, ACCESS_TOKEN_EXPIRES_IN);
    }

    private String resolveAvatar(User user) {
        if (StringUtils.hasText(user.getAvatar())) {
            return user.getAvatar();
        }
        return ownerRepository.findByUserId(user.getId())
                .map(Owner::getAvatar)
                .filter(StringUtils::hasText)
                .orElse(null);
    }

    private void validatePortal(String role, String portal) {
        if ("admin".equals(portal) && !ADMIN_PORTAL_ROLES.contains(role)) {
            throw new BusinessException(msg("auth.portal.wrong_admin"));
        }
        if ("client".equals(portal) && !CLIENT_PORTAL_ROLES.contains(role)) {
            throw new BusinessException(msg("auth.portal.wrong_client"));
        }
    }
}
