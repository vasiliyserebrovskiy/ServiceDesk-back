package com.sitool.servicedesk.status.dto.response;

import java.util.UUID;

/**
 * Status response DTO.
 */
public record StatusDto(
        UUID id,
        String name,
        String description,
        Boolean isIncident,
        Boolean isProblem,
        Boolean isRequest,
        Boolean isChange,
        Boolean isTask
) {}
