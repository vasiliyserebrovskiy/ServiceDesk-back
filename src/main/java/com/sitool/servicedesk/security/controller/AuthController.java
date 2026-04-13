package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final AuthenticationManager authenticationManager;

    @Override
    public Map<String,String> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.email(), loginUserRequest.password())
        );

        return Map.of("message","Login Successful!");
    }
}
