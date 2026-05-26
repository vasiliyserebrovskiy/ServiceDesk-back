package com.sitool.servicedesk.group.mapper;

import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.entity.Group;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto groupToGroupDto(Group group, List<UUID> userIds);
}
