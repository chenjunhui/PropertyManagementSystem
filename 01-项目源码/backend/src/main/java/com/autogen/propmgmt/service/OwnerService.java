package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final MessageSource messageSource;

    public List<Owner> list(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return ownerRepository.findByNameContaining(keyword);
        }
        return ownerRepository.findAll();
    }

    public Owner getById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.owner.not_found", null, LocaleContextHolder.getLocale())));
    }

    public Owner getByUserId(Long userId) {
        return ownerRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.owner.room_not_found", null, LocaleContextHolder.getLocale())));
    }

    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    public void delete(Long id) {
        ownerRepository.deleteById(id);
    }
}
