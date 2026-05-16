package com.sitool.servicedesk.role.repository;

import com.sitool.servicedesk.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing {@link Role} entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    /**
     * Finds the default system role.
     *
     * @return optional containing the default role if present
     */
    Optional<Role> findByDefaultRoleTrue();
    /**
     * Finds role by role name.
     *
     * @param name role name
     * @return optional role
     */
    Optional<Role> findByName(String name);

}
