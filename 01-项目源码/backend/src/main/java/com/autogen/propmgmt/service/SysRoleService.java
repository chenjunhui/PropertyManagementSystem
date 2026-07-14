package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.SysRole;
import com.autogen.propmgmt.repository.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleService {

    private final SysRoleRepository roleRepository;
    private final MessageSource messageSource;

    public List<SysRole> list() {
        return roleRepository.findAll();
    }

    public SysRole save(SysRole role) {
        if (!StringUtils.hasText(role.getRoleCode()) || !StringUtils.hasText(role.getRoleName())) {
            throw new BusinessException(messageSource.getMessage("validation.role.code_name_required", null, LocaleContextHolder.getLocale()));
        }
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
