package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.PropBuilding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropBuildingRepository extends JpaRepository<PropBuilding, Long> {
    List<PropBuilding> findByBuildingNameContaining(String keyword);
}
