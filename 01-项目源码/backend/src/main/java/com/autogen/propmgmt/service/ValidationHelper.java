package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.entity.User;
import com.autogen.propmgmt.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationHelper {

    private static final Pattern PHONE = Pattern.compile("^1\\d{10}$");
    private static final Pattern EMAIL = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern OWNER_NO = Pattern.compile("^[A-Za-z0-9]{4,20}$");

    private final OwnerRepository ownerRepository;
    private final MessageSource messageSource;

    private String msg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public void validatePhone(String phone, boolean required) {
        if (!StringUtils.hasText(phone)) {
            if (required) throw new BusinessException(msg("validation.phone.required"));
            return;
        }
        if (phone.length() != 11 || !PHONE.matcher(phone).matches()) {
            throw new BusinessException(msg("validation.phone.invalid"));
        }
    }

    public void validateEmail(String email) {
        if (StringUtils.hasText(email) && !EMAIL.matcher(email).matches()) {
            throw new BusinessException(msg("validation.email.invalid"));
        }
    }

    public void validateOwner(Owner owner, boolean isCreate) {
        if (!StringUtils.hasText(owner.getName())) {
            throw new BusinessException(msg("validation.name.required"));
        }
        if (!StringUtils.hasText(owner.getOwnerNo())) {
            throw new BusinessException(msg("validation.owner_no.required"));
        }
        if (!OWNER_NO.matcher(owner.getOwnerNo()).matches()) {
            throw new BusinessException(msg("validation.owner_no.invalid"));
        }
        if (isCreate && ownerRepository.findByOwnerNo(owner.getOwnerNo()).isPresent()) {
            throw new BusinessException(msg("validation.owner_no.duplicate"));
        }
        validatePhone(owner.getPhone(), false);
        validateEmail(owner.getEmail());
    }

    public void validateUserProfile(User user) {
        if (!StringUtils.hasText(user.getName())) {
            throw new BusinessException(msg("validation.name.required"));
        }
        validatePhone(user.getPhone(), false);
        validateEmail(user.getEmail());
    }
}
