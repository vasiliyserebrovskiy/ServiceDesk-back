package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public RegisterUserResponse createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return userService.createNewUser(registerUserRequest);
    }

    @Override
    public UserDto getUser(Authentication authentication) {
        return userService.getUser(authentication);
    }


}
