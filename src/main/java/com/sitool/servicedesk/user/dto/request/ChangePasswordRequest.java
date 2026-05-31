package com.sitool.servicedesk.user.dto.request;

import com.sitool.servicedesk.user.constraints.UserValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for changing user password in the system.
 * Contains user old password and user new password.
 */
public record ChangePasswordRequest(
        @NotBlank(message="{user.oldPassword.notBlank}")
        String oldPassword,

        @NotBlank(message="{user.password.notBlank}")
        @Size(min= UserValidationConstants.PASSWORD_MIN_LENGTH, max=UserValidationConstants.PASSWORD_MAX_LENGTH, message="{user.password.length}")
        @Pattern(
                regexp = UserValidationConstants.PASSWORD_REGEX,
                message = "{user.password.validation}"
        )
        String newPassword
) {
}
