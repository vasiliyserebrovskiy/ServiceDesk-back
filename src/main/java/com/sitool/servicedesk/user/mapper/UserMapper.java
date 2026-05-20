package com.sitool.servicedesk.user.mapper;

import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "firstname", expression = "java(user.getProfile().getFirstname())")
    @Mapping(target = "lastname", expression = "java(user.getProfile().getLastname())")
    @Mapping(target = "description", expression = "java(user.getProfile().getDescription())")
    @Mapping(target = "avatarUrl", expression = "java(user.getProfile().getAvatarUrl())")
    @Mapping(target = "roleId", expression = "java(user.getRole().getId())")
    @Mapping(target = "isActive", expression = "java(user.isActive())")
    @Mapping(target = "isBlocked", expression = "java(user.isBlocked())")
    UserDto toDto(User user);

    @Mapping(target = "firstname", expression = "java(user.getProfile().getFirstname())")
    @Mapping(target = "lastname", expression = "java(user.getProfile().getLastname())")
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    @Mapping(target = "description", expression = "java(user.getProfile().getDescription())")
    @Mapping(target = "url", expression = "java(user.getProfile().getAvatarUrl())")
    RegisterUserResponse toRegisterResponse(User user);
}