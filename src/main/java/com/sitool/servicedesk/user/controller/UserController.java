package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController implements UserApi {

    @Override
    public Map<String,String> createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        System.out.println("USER TRY TO REGISTER WITH CREDENTIALS:");
        System.out.println(registerUserRequest);
        return Map.of("message: ","success",
                "userFirstName: ", registerUserRequest.firstname(),
                "userLastName: ", registerUserRequest.lastname(),
                "userEmail: ", registerUserRequest.email(),
                "userPassword: ", registerUserRequest.password());
    }

}
