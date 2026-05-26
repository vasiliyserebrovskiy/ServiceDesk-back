package com.sitool.servicedesk.group.repository;

import com.sitool.servicedesk.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
