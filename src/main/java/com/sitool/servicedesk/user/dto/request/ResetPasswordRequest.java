package com.sitool.servicedesk.user.dto.request;

import com.sitool.servicedesk.user.constraints.UserValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for resetting user password in the system.
 * Contains user new password.
 */
public record ResetPasswordRequest(
        @NotBlank(message="{user.password.notBlank}")
        @Size(min= UserValidationConstants.PASSWORD_MIN_LENGTH, max=UserValidationConstants.PASSWORD_MAX_LENGTH, message="{user.password.length}")
        @Pattern(
                regexp = UserValidationConstants.PASSWORD_REGEX,
                message = "{user.password.validation}"
        )
        String newPassword
) {
}
