package com.sitool.servicedesk.user.validation;
import com.sitool.servicedesk.user.validation.CustomEmailValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomEmailValidatorTest {

    private CustomEmailValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new CustomEmailValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_shouldReturnTrue_forValidEmail() {

        boolean result = validator.isValid(
                "test@test.com",
                context
        );

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalse_forInvalidEmailFormat() {

        boolean result = validator.isValid(
                "wrong-email",
                context
        );

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_forEmailWithDoubleDots() {

        boolean result = validator.isValid(
                "test..mail@test.com",
                context
        );

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnTrue_forNullValue() {

        boolean result = validator.isValid(
                null,
                context
        );

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnTrue_forBlankValue() {

        boolean result = validator.isValid(
                "   ",
                context
        );

        assertTrue(result);
    }
}
