package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public RegisterUserResponse createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        System.out.println("USER TRY TO REGISTER WITH CREDENTIALS:");
        System.out.println(registerUserRequest);
        return userService.createNewUser(registerUserRequest);
    }

}
