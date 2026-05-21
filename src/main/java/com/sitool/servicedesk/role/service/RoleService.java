package com.sitool.servicedesk.role.service;

import com.sitool.servicedesk.role.dto.response.RoleDto;

import java.util.List;

/**
 * Service layer for role-related business logic.
 *
 * Provides operations for retrieving and managing system roles.
 * This service acts as an abstraction between controllers and
 * the persistence layer (repository/DAO), encapsulating all
 * business rules related to roles.
 */
public interface RoleService {

    /**
     * Retrieves all available roles in the system.
     *
     * @return list of role DTOs representing all system roles
     */
    List<RoleDto> getRoles();

    /**
     * Creates a default roles during application startup
     * if they do not already exist in the system.
     */
    void  createRolesIfNorExists();
}
