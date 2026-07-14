package com.autogen.propmgmt.service;

import com.autogen.propmgmt.entity.OperationLog;
import com.autogen.propmgmt.repository.OperationLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationLogServiceTest {

    @Mock
    private OperationLogRepository logRepository;

    @InjectMocks
    private OperationLogService logService;

    @Test
    void logSuccess() {
        logService.log("认证", "登录", "admin", "ADMIN", "登录成功", "127.0.0.1");

        verify(logRepository).save(any(OperationLog.class));
    }

    @Test
    void logError() {
        logService.logError("认证", "登录", "admin", "ADMIN", "密码错误", "127.0.0.1", "密码错误");

        verify(logRepository).save(any(OperationLog.class));
    }

    @Test
    void listLogs() {
        OperationLog log = new OperationLog();
        log.setId(1L);
        log.setModule("认证");
        log.setOperation("登录");

        Page<OperationLog> page = new PageImpl<>(Arrays.asList(log));
        when(logRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<OperationLog> result = logService.list(PageRequest.of(0, 20));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("认证", result.getContent().get(0).getModule());
    }

    @Test
    void listByModule() {
        OperationLog log = new OperationLog();
        log.setModule("认证");

        Page<OperationLog> page = new PageImpl<>(Arrays.asList(log));
        when(logRepository.findByModuleOrderByCreatedAtDesc(eq("认证"), any(PageRequest.class))).thenReturn(page);

        Page<OperationLog> result = logService.listByModule("认证", PageRequest.of(0, 20));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void listByOperator() {
        OperationLog log = new OperationLog();
        log.setOperator("admin");

        Page<OperationLog> page = new PageImpl<>(Arrays.asList(log));
        when(logRepository.findByOperatorOrderByCreatedAtDesc(eq("admin"), any(PageRequest.class))).thenReturn(page);

        Page<OperationLog> result = logService.listByOperator("admin", PageRequest.of(0, 20));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
