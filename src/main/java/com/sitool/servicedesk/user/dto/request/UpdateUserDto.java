package com.sitool.servicedesk.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitool.servicedesk.user.constraints.UserValidationConstants;
import com.sitool.servicedesk.user.validation.ValidCustomEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO for updating an existing user.
 *
 * <p>Contains editable user profile fields and account status flags
 * used in administration operations.</p>
 *
 * @param firstname user's first name
 * @param lastname user's last name
 * @param email user's email address
 * @param roleId identifier of the assigned role
 * @param isActive indicates whether the user account is active
 * @param isBlocked indicates whether the user account is blocked
 * @param description additional user description or notes
 * @param avatarUrl URL of the user's avatar image
 */
public record UpdateUserDto(
        @NotBlank(message = "{user.firstname.notBlank}")
        String firstname,
        @NotBlank(message = "{user.lastnamename.notBlank}")
        String lastname,
        @NotBlank(message = "{user.email.notBlank}")
        @Size(min= UserValidationConstants.EMAIL_MIN_LENGTH, max=UserValidationConstants.EMAIL_MAX_LENGTH, message="{user.email.length}")
        @ValidCustomEmail(message = "{user.email.validation}")
        String email,
        @NotNull(message = "{user.role.notNull}")
        UUID roleId,
        boolean isActive,
        boolean isBlocked,
        String description,
        String avatarUrl
) {
}
