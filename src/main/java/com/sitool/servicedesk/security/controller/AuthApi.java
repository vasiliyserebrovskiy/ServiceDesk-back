package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.exceptions.handling.responce.ErrorResponseDto;
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
import jakarta.servlet.http.HttpServletResponse;
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

    @Operation(summary = "Login", description = "User login process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorization successfully completed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXNfZGV2QHVwdGVhbXMuZGUiLCJleHAiOjE3NTA5Mzc5NTh9.0ADS106wR9fuLuAXCgzFyxDP4JaznPH7zCZgUy_11GQ",
                                      "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXNfZGV2QHVwdGVhbXMuZGUiLCJleHAiOjE3NTEwMjM3NDd9.joEW3Hv8E63cWYf2w-bHr7pQEgUycYpWEF-Hq0r8Yrs"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials, unconfirmed registration user deactivated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-06-26T13:41:32.3327347",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "User not found: <user email>",
                                      "path": "/api/v1/auth/login"
                                    }
                                    """) //TODO: check the correctness of a message field!
                    )
            )
    })
    @PostMapping("/login")
    TokenResponseDto login(@Valid @RequestBody LoginUserRequest loginUserRequest, HttpServletResponse response);


    @Operation(summary = "Get new access token", description = "Obtain new access token using a refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New access token granted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseDto.class)))
            ,
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": 1627660173000,
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Refresh token is invalid or expired",
                                      "path": "/api/v1/auth/refresh-token"
                                    }
                                    """))
            )
    })
    @PostMapping("/refresh-token")
    TokenResponseDto refresh(@RequestBody RefreshTokenRequest refreshToken, HttpServletResponse response);

}
