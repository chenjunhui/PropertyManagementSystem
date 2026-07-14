package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.SysMenu;
import com.autogen.propmgmt.entity.SysRole;
import com.autogen.propmgmt.entity.SysRoleMenu;
import com.autogen.propmgmt.repository.SysMenuRepository;
import com.autogen.propmgmt.repository.SysRoleMenuRepository;
import com.autogen.propmgmt.repository.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuService {

    private final SysMenuRepository menuRepository;
    private final SysRoleRepository roleRepository;
    private final SysRoleMenuRepository roleMenuRepository;
    private final MessageSource messageSource;

    public List<SysMenu> listAll() {
        return menuRepository.findAll();
    }

    public List<SysMenu> listByRole(String roleCode) {
        SysRole role = roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.role.not_found", null, LocaleContextHolder.getLocale())));
        Set<Long> menuIds = roleMenuRepository.findByRoleId(role.getId()).stream()
                .map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        return menuRepository.findByPortalAndStatusOrderBySortOrderAsc("admin", 1).stream()
                .filter(m -> menuIds.contains(m.getId()))
                .collect(Collectors.toList());
    }

    public SysMenu save(SysMenu menu) {
        if (!StringUtils.hasText(menu.getTitle()) || !StringUtils.hasText(menu.getPath())) {
            throw new BusinessException(messageSource.getMessage("validation.menu.name_path_required", null, LocaleContextHolder.getLocale()));
        }
        return menuRepository.save(menu);
    }

    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMenuRepository.deleteByRoleId(roleId);
        for (Long menuId : menuIds) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            roleMenuRepository.save(rm);
        }
    }

    public List<Long> getMenuIdsByRole(Long roleId) {
        return roleMenuRepository.findByRoleId(roleId).stream()
                .map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }
}
