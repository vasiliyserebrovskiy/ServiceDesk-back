package com.sitool.servicedesk.group.dto.request;


import com.sitool.servicedesk.group.constraints.GroupValidationConstants;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Request payload for updating a group.
 */
public record UpdateGroupRequest(

        @Size(
                min = GroupValidationConstants.NAME_MIN_LENGTH,
                max = GroupValidationConstants.NAME_MAX_LENGTH,
                message="{group.name.length}"
        )
        String name,
        String description,
        List<UUID> userIds
) {}
