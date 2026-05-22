package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.request.UpdateUserDto;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller implementation for user operations.
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto createNewUser(RegisterUserRequest registerUserRequest) {
        return userService.createNewUser(registerUserRequest);
    }

    @Override
    public UserDto getMe(UserDetails userDetails) {
        return userService.getMe(userDetails.getUsername());
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public UserDto getUser(UUID userId) {
        return userService.getUser(userId);
    }

    @Override
    public UserDto updateUser(UUID userId, UpdateUserDto updateUserDto) {
        return userService.updateUser(userId, updateUserDto);
    }


}
