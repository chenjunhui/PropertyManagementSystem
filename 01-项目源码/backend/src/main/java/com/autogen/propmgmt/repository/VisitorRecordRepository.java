package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.VisitorRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorRecordRepository extends JpaRepository<VisitorRecord, Long> {
    List<VisitorRecord> findAll();
}
