package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.InspectionRecord;
import com.autogen.propmgmt.repository.InspectionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionRecordService {

    private final InspectionRecordRepository hygieneinspectionrepository;
    private final MessageSource messageSource;

    public List<InspectionRecord> list() {
        return hygieneinspectionrepository.findAll();
    }

    public InspectionRecord getById(Long id) {
        return hygieneinspectionrepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.inspection_record.not_found", null, LocaleContextHolder.getLocale())));
    }

    public InspectionRecord save(InspectionRecord entity) {
        return hygieneinspectionrepository.save(entity);
    }

    public void delete(Long id) {
        hygieneinspectionrepository.deleteById(id);
    }

}
