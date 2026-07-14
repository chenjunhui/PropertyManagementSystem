package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.PropertyFee;
import com.autogen.propmgmt.repository.PropertyFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyFeeService {

    private final PropertyFeeRepository feeRepository;
    private final MessageSource messageSource;

    public List<PropertyFee> list() {
        return feeRepository.findAll();
    }

    public PropertyFee getById(Long id) {
        return feeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.property_fee.not_found", null, LocaleContextHolder.getLocale())));
    }

    public PropertyFee save(PropertyFee entity) {
        BigDecimal mgmt = entity.getManagementFee() != null ? entity.getManagementFee() : BigDecimal.ZERO;
        BigDecimal pub = entity.getPublicFee() != null ? entity.getPublicFee() : BigDecimal.ZERO;
        entity.setTotalFee(mgmt.add(pub));
        return feeRepository.save(entity);
    }

    public void delete(Long id) {
        feeRepository.deleteById(id);
    }
}
