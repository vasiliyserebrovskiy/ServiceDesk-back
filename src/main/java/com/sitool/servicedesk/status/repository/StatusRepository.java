package com.sitool.servicedesk.status.repository;

import com.sitool.servicedesk.status.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link Status} entities.
 * Provides CRUD operations and query methods for Category persistence.
 */
public interface StatusRepository extends JpaRepository<Status, UUID> {
    boolean existsByNameIgnoreCase(String name);
    List<Status> findAllByIsIncidentTrue();
    List<Status> findAllByIsProblemTrue();
    List<Status> findAllByIsRequestTrue();
    List<Status> findAllByIsChangeTrue();
    List<Status> findAllByIsTaskTrue();
}
