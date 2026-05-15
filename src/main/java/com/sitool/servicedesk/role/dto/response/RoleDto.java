package com.sitool.servicedesk.role.dto.response;

import java.util.UUID;

/**
 * DTO representing role information returned to the client.
 *
 * @param id unique role identifier
 * @param name role name
 * @param description role description
 * @param defaultRole indicates whether the role is assigned by default
 */
public record RoleDto(
        UUID id,
        String name,
        String description,
        boolean defaultRole
) {}
