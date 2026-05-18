package com.sitool.servicedesk.role.mapper;

import com.sitool.servicedesk.role.dto.response.RoleDto;
import com.sitool.servicedesk.role.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "defaultRole", source = "defaultRole")
    RoleDto toDto(Role role);

}
