package com.autogen.propmgmt.repository;

import com.autogen.propmgmt.entity.PropAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropAnnouncementRepository extends JpaRepository<PropAnnouncement, Long> {
    List<PropAnnouncement> findAll();
}
