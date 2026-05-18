package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.dto.response.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for user management operations.
 * <p>
 * Provides endpoints for:
 * <ul>
 *     <li>User registration</li>
 *     <li>Retrieving current authenticated user information</li>
 * </ul>
 */
@Tag(name = "User controller", description = "Controller for User operations")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @Operation(summary = "Register new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "123e4567-e89b-12d3-a456-426614174000",
                                      "firstname": "Vasiliy",
                                      "lastname": "Serebrovskii",
                                      "email": "vasiliy@domain.com",
                                      "role": "USER"
                                    }
                                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      { "field": "email", "error": "must be a well-formed email address" },
                                      { "field": "password", "error": "must not be blank" }
                                    ]
                                    """))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    RegisterUserResponse createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest);

    @Operation(
            summary = "Get current user",
            description = "Returns information about authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User info retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping("/me")
    UserDto getUser(@AuthenticationPrincipal UserDetails userDetails);
}
