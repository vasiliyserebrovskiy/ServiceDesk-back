package com.sitool.servicedesk.group.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * Group response DTO.
 */
public record GroupDto(
        UUID id,
        String name,
        String description,
        List<UUID> userIds
) {}
