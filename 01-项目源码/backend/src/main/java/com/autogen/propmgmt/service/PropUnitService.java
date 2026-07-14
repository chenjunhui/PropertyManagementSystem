package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.PropUnit;
import com.autogen.propmgmt.repository.PropUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropUnitService {

    private final PropUnitRepository unitRepository;
    private final MessageSource messageSource;

    public List<PropUnit> list(String keyword, Long buildingId) {
        if (buildingId != null) {
            return unitRepository.findByBuildingId(buildingId);
        }
        if (StringUtils.hasText(keyword)) {
            return unitRepository.findByUnitNoContaining(keyword);
        }
        return unitRepository.findAll();
    }

    public PropUnit getById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.unit.not_found", null, LocaleContextHolder.getLocale())));
    }

    public PropUnit save(PropUnit unit) {
        if (unit.getOwnerCount() == null) {
            unit.setOwnerCount(0);
        }
        return unitRepository.save(unit);
    }

    public void delete(Long id) {
        unitRepository.deleteById(id);
    }
}
