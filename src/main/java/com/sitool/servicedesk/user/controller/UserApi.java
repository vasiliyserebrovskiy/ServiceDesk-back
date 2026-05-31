package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import com.sitool.servicedesk.security.service.AuthUserDetails;
import com.sitool.servicedesk.user.dto.request.ChangePasswordRequest;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.request.ResetPasswordRequest;
import com.sitool.servicedesk.user.dto.request.UpdateUserDto;
import com.sitool.servicedesk.user.dto.response.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

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
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "767ea865-8b32-454c-af05-52508be4033c",
                                      "firstname": "Vasiliy",
                                      "lastname": "Serebrovskii",
                                      "email": "vasiliy@domain.com",
                                      "description":"some description",
                                      "avatar_url":"https://avatar.jpg",
                                      "role_id": "3453d552-f904-4f46-91c4-6b782553b421",
                                      "is_active":true,
                                      "is_blocked": false
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
    @PostMapping
    UserDto createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest);

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
    UserDto getMe(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping
    List<UserDto> getAllUsers();

    @Operation(
            summary = "Get user by id",
            description = "Returns user information for the specified userId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{userId}")
    UserDto getUser(
            @Parameter(
                    description = "Unique user identifier (UUID format)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID userId
    );

    @Operation(
            summary = "Update user",
            description = "Updates editable information for the specified userId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{userId}")
    UserDto updateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserDto updateUserDto);

    @Operation(
            summary = "Change password",
            description = "Allows authenticated user to change their own password."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Password changed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - can only change own password"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changePassword(@PathVariable UUID userId, @Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal AuthUserDetails authUser);

    @Operation(
            summary = "Reset user password",
            description = "Allows administrator to reset password for a specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Password reset successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - admin role required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PostMapping("/{userId}/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void resetPassword(@PathVariable UUID userId, @Valid @RequestBody ResetPasswordRequest request);
}
