package com.sitool.servicedesk.role.service;

import com.sitool.servicedesk.role.dto.response.RoleDto;
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
}
