package com.sitool.servicedesk.incident.repository;

import com.sitool.servicedesk.incident.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link Incident} entities.
 * Provides CRUD operations and query methods for Incident persistence.
 */
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    @Query(value = "SELECT nextval('incident_number_seq')", nativeQuery = true)
    Long getNextNumber();

    boolean existsByNumber(String number);

    List<Incident> findAllByOrderByCreatedAtDesc();
}
