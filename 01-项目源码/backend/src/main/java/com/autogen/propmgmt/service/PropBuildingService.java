package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.PropBuilding;
import com.autogen.propmgmt.repository.PropBuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropBuildingService {

    private final PropBuildingRepository buildingRepository;
    private final MessageSource messageSource;

    public List<PropBuilding> list(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return buildingRepository.findByBuildingNameContaining(keyword);
        }
        return buildingRepository.findAll();
    }

    public PropBuilding getById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.building.not_found", null, LocaleContextHolder.getLocale())));
    }

    public PropBuilding save(PropBuilding building) {
        return buildingRepository.save(building);
    }

    public void delete(Long id) {
        buildingRepository.deleteById(id);
    }
}
