package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.RepairRequest;
import com.autogen.propmgmt.repository.RepairRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final MessageSource messageSource;

    public List<RepairRequest> list(String status) {
        if (status != null && !status.isBlank()) {
            return repairRequestRepository.findByStatus(status);
        }
        return repairRequestRepository.findAll();
    }

    public List<RepairRequest> listByOwner(Long ownerId) {
        return repairRequestRepository.findByOwnerId(ownerId);
    }

    public RepairRequest getById(Long id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(messageSource.getMessage("entity.repair_request.not_found", null, LocaleContextHolder.getLocale())));
    }

    public RepairRequest save(RepairRequest request) {
        return repairRequestRepository.save(request);
    }

    public RepairRequest updateStatus(Long id, String status, String remark) {
        RepairRequest request = getById(id);
        request.setStatus(status);
        if (remark != null) {
            request.setRemark(remark);
        }
        if ("DONE".equals(status)) {
            request.setFinishDate(LocalDate.now());
        }
        return repairRequestRepository.save(request);
    }

    public void delete(Long id) {
        repairRequestRepository.deleteById(id);
    }
}
