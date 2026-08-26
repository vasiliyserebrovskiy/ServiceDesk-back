package com.sitool.servicedesk.ci.repository;

import com.sitool.servicedesk.ci.entity.CI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link CI} entities.
 * Provides CRUD operations and query methods for Configuration item persistence.
 */
public interface CIRepository extends JpaRepository<CI, UUID> {
    boolean existsByNameIgnoreCase(String name);

    @Query("select c.id from CI c where c.serialNumber = :serialNumber")
    Optional<UUID> findIdBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query("select c.id from CI c where c.name = :name")
    Optional<UUID> findIdByName(@Param("name") String name);
}
