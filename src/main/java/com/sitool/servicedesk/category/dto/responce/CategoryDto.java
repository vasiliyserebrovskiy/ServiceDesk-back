package com.sitool.servicedesk.category.dto.responce;

import java.util.UUID;

/**
 * Category response DTO.
 */
public record CategoryDto(
        UUID id,
        String name,
        String description,
        String isIncident,
        String isProblem,
        String isRequest,
        String isChange
) {}
