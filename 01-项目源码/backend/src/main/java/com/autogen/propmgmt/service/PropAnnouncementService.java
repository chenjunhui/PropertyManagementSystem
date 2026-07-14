package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.PropAnnouncement;
import com.autogen.propmgmt.repository.PropAnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropAnnouncementService {

    private final PropAnnouncementRepository dormannouncementrepository;
    private final MessageSource messageSource;

    public List<PropAnnouncement> list() {
        return dormannouncementrepository.findAll();
    }

    public PropAnnouncement getById(Long id) {
        return dormannouncementrepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.announcement.not_found", null, LocaleContextHolder.getLocale())));
    }

    public PropAnnouncement save(PropAnnouncement entity) {
        return dormannouncementrepository.save(entity);
    }

    public void delete(Long id) {
        dormannouncementrepository.deleteById(id);
    }

}
