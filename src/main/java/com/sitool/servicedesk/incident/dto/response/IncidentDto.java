package com.sitool.servicedesk.incident.dto.response;

import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Incident response DTO.
 */
public record IncidentDto(
        UUID id,
        String number,
        UUID requesterId,
        UUID categoryId,
        UUID subcategoryId,
        UUID statusId,
        Priority priority,
        Impact impact,
        Urgency urgency,
        UUID ciId,
        UUID groupId,
        UUID assigneeId,
        String shortDescription,
        String description,
        String servicenowNumber,
        Boolean servicenowSynced,
        LocalDateTime servicenowSyncedAt,
        LocalDateTime createdAt,
        String closeComment,
        LocalDateTime actualStart,
        LocalDateTime actualEnd
) {
}
