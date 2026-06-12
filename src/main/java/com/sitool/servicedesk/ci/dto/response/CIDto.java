package com.sitool.servicedesk.ci.dto.response;

import java.util.UUID;

/**
 * CI response DTO.
 */
public record CIDto(
        UUID id,
        String name,
        String description,
        String type,
        String manufacturer,
        String serialNumber,
        String model
) {}
