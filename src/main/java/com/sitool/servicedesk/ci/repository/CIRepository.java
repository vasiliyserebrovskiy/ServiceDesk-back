package com.sitool.servicedesk.ci.repository;

import com.sitool.servicedesk.ci.entity.CI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link CI} entities.
 * Provides CRUD operations and query methods for Configuration item persistence.
 */
public interface CIRepository extends JpaRepository<CI, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Optional<UUID> findIdBySerialNumber(String serialNumber);
    Optional<UUID> findIdByName(String name);
}
