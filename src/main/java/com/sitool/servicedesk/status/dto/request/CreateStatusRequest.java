package com.sitool.servicedesk.status.dto.request;

import com.sitool.servicedesk.status.constraints.StatusValidationConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request payload for creating a status.
 */
public record CreateStatusRequest(
        @NotBlank(message = "{status.name.notBlank}")
        @Size(
                min = StatusValidationConstraints.NAME_MIN_LENGTH,
                max = StatusValidationConstraints.NAME_MAX_LENGTH,
                message = "{status.name.length}"
        )
        String name,
        String description,
        @NotNull(message = "{status.isIncident.notNull}")
        Boolean isIncident,
        @NotNull(message = "{status.isProblem.notNull}")
        Boolean isProblem,
        @NotNull(message = "{status.isRequest.notNull}")
        Boolean isRequest,
        @NotNull(message = "{status.isChange.notNull}")
        Boolean isChange,
        @NotNull(message = "{status.isTask.notNull}")
        Boolean isTask
) {
}
