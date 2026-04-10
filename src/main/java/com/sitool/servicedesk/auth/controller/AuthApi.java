package com.sitool.servicedesk.auth.controller;

import com.sitool.servicedesk.auth.dto.request.LoginUserRequest;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Controller responsible for authentication-related endpoints.
 *
 * Endpoints:
 * <ul>
 *   <li>POST /auth/login - login()</li>
 * </ul>
 */
@Tag(name = "Authorization controller", description = "Controller for User authorization")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @PostMapping("/login")
    Map<String,String> login(@Valid @RequestBody LoginUserRequest loginUserRequest);
}
