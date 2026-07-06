package com.sitool.servicedesk.servicenow.settings.dto.response;

import java.util.UUID;

/**
 * ServiceNow Settings response DTO.
 */
public record ServiceNowSettingsDto(
        UUID id,
        String endpoint,
        String username,
        Boolean passwordConfigured
) {}
