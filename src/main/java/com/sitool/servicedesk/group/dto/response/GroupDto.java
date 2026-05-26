package com.sitool.servicedesk.group.dto.response;

import com.sitool.servicedesk.usergroup.entity.UserGroupId;

import java.util.List;
import java.util.UUID;

public record GroupDto(
        UUID id,
        String name,
        String description,
        List<UUID> userIds
) {}
