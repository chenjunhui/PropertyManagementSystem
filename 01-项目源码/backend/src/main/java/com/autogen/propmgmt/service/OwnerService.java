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
    private final OperationLogService logService;

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
        Owner saved = ownerRepository.save(owner);
        if (owner.getId() == null) {
            logService.log("业主", "新增业主", null, null, "姓名: " + owner.getName(), null);
        } else {
            logService.log("业主", "更新业主", null, null, "ID: " + owner.getId() + ", 姓名: " + owner.getName(), null);
        }
        return saved;
    }

    public void delete(Long id) {
        ownerRepository.deleteById(id);
        logService.log("业主", "删除业主", null, null, "ID: " + id, null);
    }
}
