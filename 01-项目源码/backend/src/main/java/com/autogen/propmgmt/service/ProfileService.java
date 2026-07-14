package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.dto.ProfileUpdateRequest;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.entity.User;
import com.autogen.propmgmt.repository.OwnerRepository;
import com.autogen.propmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final ValidationHelper validationHelper;
    private final MessageSource messageSource;

    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.user.not_found", null, LocaleContextHolder.getLocale())));
    }

    @Transactional
    public User updateUserProfile(Long userId, ProfileUpdateRequest req) {
        User user = getUserProfile(userId);
        if (req.getName() != null) user.setName(req.getName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        user.setAvatar(StringUtils.hasText(req.getAvatar()) ? req.getAvatar().trim() : null);
        validationHelper.validateUserProfile(user);
        return userRepository.save(user);
    }

    @Transactional
    public Owner updateOwnerProfile(Long ownerId, ProfileUpdateRequest req) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.owner.not_found", null, LocaleContextHolder.getLocale())));
        if (req.getName() != null) owner.setName(req.getName());
        if (req.getPhone() != null) owner.setPhone(req.getPhone());
        if (req.getEmail() != null) owner.setEmail(req.getEmail());
        owner.setAvatar(StringUtils.hasText(req.getAvatar()) ? req.getAvatar().trim() : null);
        validationHelper.validatePhone(owner.getPhone(), false);
        validationHelper.validateEmail(owner.getEmail());
        Owner saved = ownerRepository.save(owner);
        if (owner.getUserId() != null) {
            userRepository.findById(owner.getUserId()).ifPresent(u -> {
                u.setName(saved.getName());
                u.setPhone(saved.getPhone());
                u.setEmail(saved.getEmail());
                u.setAvatar(saved.getAvatar());
                userRepository.save(u);
            });
        }
        return saved;
    }
}
