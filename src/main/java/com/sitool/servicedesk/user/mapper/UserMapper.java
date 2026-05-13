package com.sitool.servicedesk.user.mapper;

import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "url",
            expression = "java(user.getAvatarUrl())")
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    UserDto toDto(User user);

    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    RegisterUserResponse toRegisterResponse(User user);
}