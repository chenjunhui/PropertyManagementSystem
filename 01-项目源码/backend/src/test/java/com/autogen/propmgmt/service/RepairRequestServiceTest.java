package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.RepairRequest;
import com.autogen.propmgmt.repository.RepairRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepairRequestServiceTest {

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private OperationLogService logService;

    @InjectMocks
    private RepairRequestService repairRequestService;

    private RepairRequest testRepair;

    @BeforeEach
    void setUp() {
        testRepair = new RepairRequest();
        testRepair.setId(1L);
        testRepair.setTitle("水龙头漏水");
        testRepair.setStatus("PENDING");
        testRepair.setOwnerId(1L);
    }

    @Test
    void listAll() {
        when(repairRequestRepository.findAll()).thenReturn(Arrays.asList(testRepair));

        List<RepairRequest> result = repairRequestService.list(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("水龙头漏水", result.get(0).getTitle());
    }

    @Test
    void listByStatus() {
        when(repairRequestRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(testRepair));

        List<RepairRequest> result = repairRequestService.list("PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getByIdSuccess() {
        when(repairRequestRepository.findById(1L)).thenReturn(Optional.of(testRepair));

        RepairRequest result = repairRequestService.getById(1L);

        assertNotNull(result);
        assertEquals("水龙头漏水", result.getTitle());
    }

    @Test
    void getByIdNotFound() {
        when(repairRequestRepository.findById(999L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("报修记录不存在");

        assertThrows(BusinessException.class, () -> repairRequestService.getById(999L));
    }

    @Test
    void saveRepair() {
        when(repairRequestRepository.save(any())).thenReturn(testRepair);

        RepairRequest result = repairRequestService.save(testRepair);

        assertNotNull(result);
        verify(repairRequestRepository).save(any());
        verify(logService).log(anyString(), anyString(), any(), any(), anyString(), any());
    }

    @Test
    void updateStatusToDone() {
        when(repairRequestRepository.findById(1L)).thenReturn(Optional.of(testRepair));
        when(repairRequestRepository.save(any())).thenReturn(testRepair);

        RepairRequest result = repairRequestService.updateStatus(1L, "DONE", "已修复");

        assertNotNull(result);
        assertEquals("DONE", result.getStatus());
        assertEquals(LocalDate.now(), result.getFinishDate());
        verify(repairRequestRepository).save(any());
    }

    @Test
    void deleteRepair() {
        repairRequestService.delete(1L);

        verify(repairRequestRepository).deleteById(1L);
        verify(logService).log(anyString(), anyString(), any(), any(), anyString(), any());
    }
}
