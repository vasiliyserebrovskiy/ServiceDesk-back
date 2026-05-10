package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.exceptions.handling.response.ErrorResponseDto;
import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.request.RefreshTokenRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Authentication controller defining endpoints for user authentication flow.
 *
 * <p>This API uses JWT stored in HttpOnly cookies instead of response body.
 * Tokens are set and managed by the server via HttpServletResponse.</p>
 *
 * <p>Authentication flow includes:
 * <ul>
 *   <li>Login - issues access and refresh tokens as cookies</li>
 *   <li>Refresh token - issues new access token using refresh cookie</li>
 *   <li>Logout - clears authentication cookies</li>
 * </ul>
 */
@Tag(name = "Authorization controller", description = "Controller for User authorization")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Operation(
            summary = "User login",
            description = "Authenticates user and sets JWT tokens in HttpOnly cookies"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful. Access and refresh tokens are stored in HttpOnly cookies.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(value = """
                        {
                          "timestamp": "2025-06-26T13:41:32.3327347",
                          "status": 401,
                          "error": "Unauthorized",
                          "message": "Invalid username or password.",
                          "path": "/api/v1/auth/login"
                        }
                        """)
                    )
            )
    })
    @PostMapping("/login")
    ResponseEntity<Void> login(@Valid @RequestBody LoginUserRequest loginUserRequest, HttpServletResponse response);


    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using refresh token from HttpOnly cookie"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token successfully refreshed. Token is stored in HttpOnly cookie.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/refresh-token")
    ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response);

    @Operation(
            summary = "User logout",
            description = "Clears authentication cookies (access and refresh tokens)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout successful. Authentication cookies cleared.",
                    content = @Content
            )
    })
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response);


}
