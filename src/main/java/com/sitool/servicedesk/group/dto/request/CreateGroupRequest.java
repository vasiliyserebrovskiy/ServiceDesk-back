package com.sitool.servicedesk.group.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sitool.servicedesk.group.constraints.GroupValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

/**
 * Request payload for creating a group.
 */
public record CreateGroupRequest(
        @NotBlank(message = "{group.name.notBlank}")
        @Size(
                min = GroupValidationConstants.NAME_MIN_LENGTH,
                max = GroupValidationConstants.NAME_MAX_LENGTH,
                message="{group.name.length}"
        )
        String name,
        String description,
        List<UUID> userIds
) {}
