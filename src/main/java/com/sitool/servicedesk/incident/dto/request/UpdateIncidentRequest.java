package com.sitool.servicedesk.incident.dto.request;

import com.sitool.servicedesk.incident.constraints.IncidentValidationConstants;
import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request payload for updating an incident.
 */
public record UpdateIncidentRequest(
        @NotNull(message = "{incident.requesterId.notNull}")
        UUID requesterId,
        @NotNull(message = "{incident.categoryId.notNull}")
        UUID categoryId,
        UUID subcategoryId,
        @NotNull(message = "{incident.statusId.notNull}")
        UUID statusId,
        @NotNull(message = "{incident.priority.notNull}")
        Priority priority,
        @NotNull(message = "{incident.impact.notNull}")
        Impact impact,
        @NotNull(message = "{incident.urgency.notNull}")
        Urgency urgency,
        UUID ciId,
        UUID groupId,
        UUID assigneeId,
        @NotBlank(message = "{incident.shortDescription.notBlank}")
        @Size(
                min = IncidentValidationConstants.SHORT_DESCRIPTION_MIN_LENGTH,
                max = IncidentValidationConstants.SHORT_DESCRIPTION_MAX_LENGTH,
                message="{incident.shortDescription.length}"
        )
        String shortDescription,
        String description,
        String closeComment,
        LocalDateTime actualStart,
        LocalDateTime actualEnd
) {}
