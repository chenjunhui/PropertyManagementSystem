package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUserId(Long userId);

    Optional<Owner> findByOwnerNo(String ownerNo);

    List<Owner> findByNameContaining(String keyword);
}
