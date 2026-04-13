package com.sitool.servicedesk.security.validation;

import com.sitool.servicedesk.security.constraints.UserValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * This class was created to validate email address. Here we need to check RegEx pattern, and then we check ".." in email.
 * This solution was build because we can not check two dots in RegEx.
 */
public class CustomEmailValidator implements ConstraintValidator<ValidCustomEmail, String> {
    private static final String REGEX = UserValidationConstants.EMAIL_REGEX;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Regex validation
        if (!value.matches(REGEX)) return false;

        // Validate two dots
        return !value.contains("..");
    }
}
