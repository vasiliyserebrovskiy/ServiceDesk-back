package com.sitool.servicedesk.user.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenDtoIsValid() {
        UpdateUserDto dto = new UpdateUserDto(
                "John",
                "Doe",
                "john.doe@example.com",
                UUID.randomUUID(),
                true,
                false,
                "Some description",
                "https://example.com/avatar.png"
        );

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenFirstnameIsBlank() {
        UpdateUserDto dto = new UpdateUserDto(
                "",
                "Doe",
                "john.doe@example.com",
                UUID.randomUUID(),
                true,
                false,
                null,
                null
        );

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<UpdateUserDto> violation = violations.iterator().next();

        assertEquals("firstname", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailValidationWhenLastnameIsBlank() {
        UpdateUserDto dto = new UpdateUserDto(
                "John",
                "",
                "john.doe@example.com",
                UUID.randomUUID(),
                true,
                false,
                null,
                null
        );

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        UpdateUserDto dto = new UpdateUserDto(
                "John",
                "Doe",
                "",
                UUID.randomUUID(),
                true,
                false,
                null,
                null
        );

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenRoleIdIsNull() {
        UpdateUserDto dto = new UpdateUserDto(
                "John",
                "Doe",
                "john.doe@example.com",
                null,
                true,
                false,
                null,
                null
        );

        Set<ConstraintViolation<UpdateUserDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<UpdateUserDto> violation = violations.iterator().next();

        assertEquals("roleId", violation.getPropertyPath().toString());
    }
}