package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.PropertyFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyFeeRepository extends JpaRepository<PropertyFee, Long> {
    List<PropertyFee> findAll();
}
