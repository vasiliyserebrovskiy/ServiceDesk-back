package com.sitool.servicedesk.security.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for LoginUserRequest DTO
 */
@DisplayName("LoginUserRequest class tests.")
public class LoginUserRequestValidationTests {

    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Email is null
     */
    @Test
    @DisplayName("User email is null → validation test failed with message: Email cannot be blank.")
    public void emailIsNullValidationFailedWithMessage() {
        LoginUserRequest dto = new LoginUserRequest( null, "1qaZXsw@");
        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Email is blank
     */
    @Test
    @DisplayName("User email is blank → validation test failed with message: Email cannot be blank.")
    public void emailIsBlankValidationFailedWithMessage() {
        LoginUserRequest dto = new LoginUserRequest( "", "1qaZXsw@");
        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 3: Password is null
     */
    @Test
    @DisplayName("User password is null → validation test failed with message: Password cannot be blank.")
    public void passwordIsNullValidationFailedWithMessage() {
        LoginUserRequest dto = new LoginUserRequest( "vasiliy@domain.com", null);
        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: Password is blank
     */
    @Test
    @DisplayName("User password is blank → validation test failed with message: Password cannot be blank.")
    public void passwordIsBlankValidationFailedWithMessage() {
        LoginUserRequest dto = new LoginUserRequest("vasiliy@domain.com", "");
        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void LoginUserRequestIsOk() {
        LoginUserRequest dto = new LoginUserRequest("vasiliy.serebr@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<LoginUserRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
