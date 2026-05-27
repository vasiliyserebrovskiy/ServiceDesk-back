package com.sitool.servicedesk.group.mapper;

import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.entity.Group;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

/**
 * Mapper for converting {@link Group} entities to {@link GroupDto}.
 * <p>
 * Also enriches DTO with list of user IDs belonging to the group.
 */
@Mapper(componentModel = "spring")
public interface GroupMapper {
    /**
     * Converts a {@link Group} entity into {@link GroupDto}.
     *
     * @param group   the group entity
     * @param userIds list of user IDs associated with the group
     * @return mapped {@link GroupDto}
     */
    GroupDto groupToGroupDto(Group group, List<UUID> userIds);
}
