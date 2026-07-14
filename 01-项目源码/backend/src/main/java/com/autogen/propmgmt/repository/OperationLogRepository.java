package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    Page<OperationLog> findByModuleOrderByCreatedAtDesc(String module, Pageable pageable);
    Page<OperationLog> findByOperatorOrderByCreatedAtDesc(String operator, Pageable pageable);
    List<OperationLog> findTop100ByOrderByCreatedAtDesc();
}
