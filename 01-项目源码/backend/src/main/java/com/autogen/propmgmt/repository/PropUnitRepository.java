package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.PropUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropUnitRepository extends JpaRepository<PropUnit, Long> {
    List<PropUnit> findByUnitNoContaining(String keyword);

    List<PropUnit> findByBuildingId(Long buildingId);
}
