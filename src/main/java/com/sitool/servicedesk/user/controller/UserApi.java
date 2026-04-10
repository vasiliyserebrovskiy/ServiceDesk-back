package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.exeption.handling.responce.ValidationErrorDto;
import com.sitool.servicedesk.exeption.handling.responce.ErrorResponseDto;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Controller responsible for users-related endpoints.
 *
 * Endpoints:
 * <ul>
 *   <li>POST /users/register - createNewUser()</li>
 * </ul>
 */

@Tag(name = "User controller", description = "Controller for User operations")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @Operation(summary = "Register new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "firstname": "Vasiliy",
                                      "lastname": "tes_dev@upteams.de",
                                      "email": "vasiliy@domain.com",
                                      "password": "password"
                                    }
                                    """))
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
    @PostMapping("/register")
    Map<String,String> createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest);
}
