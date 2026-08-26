package com.sitool.servicedesk.group.repository;

import com.sitool.servicedesk.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link Group} entities.
 * Provides CRUD operations and query methods for Group persistence.
 */
public interface GroupRepository extends JpaRepository<Group, UUID> {
    /**
     * Checks whether a group with the given name already exists (case-insensitive).
     *
     * @param name group name to check
     * @return true if a group with this name exists, otherwise false
     */
    boolean existsByNameIgnoreCase(String name);

    @Query("select g.id from Group g where g.name = :name")
    Optional<UUID> findIdByName(@Param("name") String name);
}
