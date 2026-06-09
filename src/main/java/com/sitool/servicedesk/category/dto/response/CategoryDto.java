package com.sitool.servicedesk.category.dto.response;

import java.util.UUID;

/**
 * Category response DTO.
 */
public record CategoryDto(
        UUID id,
        String name,
        String description,
        Boolean isIncident,
        Boolean isProblem,
        Boolean isRequest,
        Boolean isChange
) {}
