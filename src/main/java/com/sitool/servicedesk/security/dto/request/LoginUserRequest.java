package com.sitool.servicedesk.security.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


/**
 * User login DTO
 * @param email
 * @param password
 */
@Schema(description = "Data Transfer Object for login user entity")
public record LoginUserRequest(
        @Schema(description = "User's email address", example = "tes_dev@upteams.de")
        @NotBlank(message = "{user.email.notBlank}")
        String email,

        @Schema(description = "User's password", example = "dev_TR_pass_007")
        @NotBlank(message="{user.password.notBlank}")
        String password
) {
}
