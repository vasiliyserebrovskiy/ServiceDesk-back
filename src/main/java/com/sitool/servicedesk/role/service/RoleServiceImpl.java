package com.sitool.servicedesk.role.service;

import com.sitool.servicedesk.role.dto.response.RoleDto;
import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.mapper.RoleMapper;
import com.sitool.servicedesk.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of RoleService.
 *
 * Responsible for handling role-related business logic and
 * delegating data access to RoleRepository.
 *
 * Uses RoleMapper to convert entity objects into RoleDto
 * for safe exposure to API layer.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Retrieves all roles from the database and maps them to DTOs.
     *
     * @return list of RoleDto representing all roles in the system
     */
    @Override
    public List<RoleDto> getRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    /**
     * Creates default system roles if they do not already exist in the database.
     * <p>
     * This method is idempotent: calling it multiple times will not create duplicate roles,
     * since existence is checked by role name before insertion.
     * </p>
     */
    @Override
    public void createRolesIfNorExists() {
        createDefaultRole("USER", "User", "Default user role",true);
        createDefaultRole("MANAGER", "Manager", "Manager user role",false);
        createDefaultRole("ADMIN", "Administrator", "Administrator user role",false);
    }

    /**
     * Creates a default role in the system if a role with the given name does not already exist.
     * <p>
     * This method performs a uniqueness check by role name before creating and saving the entity.
     * It is intended to be used only during application initialization.
     * </p>
     *
     * @param name        technical role name (used for identification and security checks)
     * @param displayName human-readable role name shown in UI
     * @param description  role description for documentation or admin UI
     * @param isDefault    whether this role is the default role assigned to new users
     */
    private void createDefaultRole(String name, String displayName, String description,  boolean isDefault) {

        if(roleRepository.findByName(name).isPresent()) {
            return;
        }
        Role role = new Role();
        role.setName(name);
        role.setDisplayName(displayName);
        role.setDescription(description);
        role.setDefaultRole(isDefault);
        roleRepository.save(role);
    }

}
