package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    List<RepairRequest> findByOwnerId(Long ownerId);

    List<RepairRequest> findByStatus(String status);
}
