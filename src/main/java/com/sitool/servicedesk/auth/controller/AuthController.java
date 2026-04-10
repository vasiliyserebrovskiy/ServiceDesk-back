package com.sitool.servicedesk.auth.controller;

import com.sitool.servicedesk.auth.dto.request.LoginUserRequest;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class AuthController implements AuthApi{

    @Override
    public Map<String,String> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        System.out.println("USER TRY TO LOGIN WITH CREDENTIALS:");
        System.out.println(loginUserRequest);
        return Map.of("message","success",
                "userEmail: ", loginUserRequest.email(),
                "userPassword: ", loginUserRequest.password());
    }
}
