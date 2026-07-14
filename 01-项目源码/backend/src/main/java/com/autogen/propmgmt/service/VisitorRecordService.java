package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.VisitorRecord;
import com.autogen.propmgmt.repository.VisitorRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorRecordService {

    private final VisitorRecordRepository visitorrecordrepository;
    private final MessageSource messageSource;

    public List<VisitorRecord> list() {
        return visitorrecordrepository.findAll();
    }

    public VisitorRecord getById(Long id) {
        return visitorrecordrepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.visitor_record.not_found", null, LocaleContextHolder.getLocale())));
    }

    public VisitorRecord save(VisitorRecord entity) {
        return visitorrecordrepository.save(entity);
    }

    public void delete(Long id) {
        visitorrecordrepository.deleteById(id);
    }

}
