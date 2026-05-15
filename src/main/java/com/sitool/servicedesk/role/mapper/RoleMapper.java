package com.sitool.servicedesk.role.mapper;

import com.sitool.servicedesk.role.dto.response.RoleDto;
import com.sitool.servicedesk.role.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role role);

}
