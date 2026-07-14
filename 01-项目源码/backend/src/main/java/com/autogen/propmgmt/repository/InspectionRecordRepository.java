package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long> {
    List<InspectionRecord> findAll();
}
