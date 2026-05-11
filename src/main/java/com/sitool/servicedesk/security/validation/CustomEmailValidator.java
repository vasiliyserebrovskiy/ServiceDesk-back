package com.sitool.servicedesk.security.validation;

import com.sitool.servicedesk.security.constraints.UserValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator for email addresses.
 *
 * <p>Performs regex validation and additionally rejects
 * email addresses containing consecutive dots.</p>
 */
public class CustomEmailValidator implements ConstraintValidator<ValidCustomEmail, String> {
    private static final String REGEX = UserValidationConstants.EMAIL_REGEX;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Regex validation
        if (!value.matches(REGEX)) {
            return false;
        }

        // Validate two dots
        return !value.contains("..");
    }
}
