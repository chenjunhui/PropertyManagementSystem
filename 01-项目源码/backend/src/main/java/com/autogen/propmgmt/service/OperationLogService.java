package com.autogen.propmgmt.service;

import com.autogen.propmgmt.entity.OperationLog;
import com.autogen.propmgmt.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationLogService {
    private final OperationLogRepository logRepository;

    public void log(String module, String operation, String operator, String operatorRole, String detail, String ip) {
        OperationLog log = new OperationLog();
        log.setModule(module);
        log.setOperation(operation);
        log.setOperator(operator);
        log.setOperatorRole(operatorRole);
        log.setDetail(detail);
        log.setIp(ip);
        log.setStatus(1);
        logRepository.save(log);
    }

    public void logError(String module, String operation, String operator, String operatorRole, String detail, String ip, String errorMsg) {
        OperationLog log = new OperationLog();
        log.setModule(module);
        log.setOperation(operation);
        log.setOperator(operator);
        log.setOperatorRole(operatorRole);
        log.setDetail(detail);
        log.setIp(ip);
        log.setStatus(0);
        log.setErrorMsg(errorMsg);
        logRepository.save(log);
    }

    public Page<OperationLog> list(Pageable pageable) {
        return logRepository.findAll(pageable);
    }

    public Page<OperationLog> listByModule(String module, Pageable pageable) {
        return logRepository.findByModuleOrderByCreatedAtDesc(module, pageable);
    }

    public Page<OperationLog> listByOperator(String operator, Pageable pageable) {
        return logRepository.findByOperatorOrderByCreatedAtDesc(operator, pageable);
    }
}
