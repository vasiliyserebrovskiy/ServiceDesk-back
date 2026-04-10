package com.sitool.servicedesk.user.dto.request;

import com.sitool.servicedesk.auth.constraints.UserValidationConstants;
import com.sitool.servicedesk.auth.validation.ValidCustomEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * User register DTO
 * @param firstname
 * @param lastname
 * @param email
 * @param password
 */
@Schema(description = "Data Transfer Object for register new user entity")
public record RegisterUserRequest(
        @Schema(
                description = "New user first name",
                example = "Vasilii"
        )
        @NotBlank(message = "{user.firstname.notBlank}")
        @Size(min = UserValidationConstants.FIRSTNAME_MIN, max = UserValidationConstants.FIRSTNAME_MAX, message="{user.firstname.length}")
        String firstname,
        @Schema(
                description = "New user last name",
                example = "Serebrovskii"
        )
        @NotBlank(message = "{user.lastname.notBlank}")
        @Size(min = UserValidationConstants.LASTNAME_MIN, max = UserValidationConstants.LASTNAME_MAX, message="{user.lastname.length}")
        String lastname,
        @Schema(
                description = "New user email",
                example = "vasiliy@domain.com"
        )
        @NotBlank(message = "{user.email.notBlank}")
        @Size(min=UserValidationConstants.EMAIL_MIN_LENGTH, max=UserValidationConstants.EMAIL_MAX_LENGTH, message="{user.email.length}")
        @ValidCustomEmail(message = "{user.email.validation}")
        String email,
        @Schema(
                description = "New user password",
                example = "1qaZ_Xsw@"
        )
        @NotBlank(message="{user.password.notBlank}")
        @Size(min= UserValidationConstants.PASSWORD_MIN_LENGTH, max=UserValidationConstants.PASSWORD_MAX_LENGTH, message="{user.password.length}")
        @Pattern(
                regexp = UserValidationConstants.PASSWORD_REGEX,
                message = "{user.password.validation}"
        )
        String password
) {
}
