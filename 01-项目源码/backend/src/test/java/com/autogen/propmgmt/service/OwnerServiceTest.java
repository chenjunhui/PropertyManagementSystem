package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import com.autogen.propmgmt.entity.Owner;
import com.autogen.propmgmt.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private OperationLogService logService;

    @InjectMocks
    private OwnerService ownerService;

    private Owner testOwner;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setName("张三");
        testOwner.setUserId(1L);
        testOwner.setPhone("13800138000");
    }

    @Test
    void listAll() {
        when(ownerRepository.findAll()).thenReturn(Arrays.asList(testOwner));

        List<Owner> result = ownerService.list(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("张三", result.get(0).getName());
    }

    @Test
    void listByKeyword() {
        when(ownerRepository.findByNameContaining("张")).thenReturn(Arrays.asList(testOwner));

        List<Owner> result = ownerService.list("张");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getByIdSuccess() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(testOwner));

        Owner result = ownerService.getById(1L);

        assertNotNull(result);
        assertEquals("张三", result.getName());
    }

    @Test
    void getByIdNotFound() {
        when(ownerRepository.findById(999L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("业主不存在");

        assertThrows(BusinessException.class, () -> ownerService.getById(999L));
    }

    @Test
    void saveNewOwner() {
        Owner newOwner = new Owner();
        newOwner.setName("李四");

        when(ownerRepository.save(any())).thenReturn(newOwner);

        Owner result = ownerService.save(newOwner);

        assertNotNull(result);
        verify(ownerRepository).save(any());
        verify(logService).log(anyString(), eq("新增业主"), any(), any(), anyString(), any());
    }

    @Test
    void updateExistingOwner() {
        when(ownerRepository.save(any())).thenReturn(testOwner);

        Owner result = ownerService.save(testOwner);

        assertNotNull(result);
        verify(ownerRepository).save(any());
        verify(logService).log(anyString(), eq("更新业主"), any(), any(), anyString(), any());
    }

    @Test
    void deleteOwner() {
        ownerService.delete(1L);

        verify(ownerRepository).deleteById(1L);
        verify(logService).log(anyString(), eq("删除业主"), any(), any(), anyString(), any());
    }
}
