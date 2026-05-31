package com.sitool.servicedesk.user.dto.request;

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
 * Validation tests for ChangePasswordRequest
 */
@DisplayName("ChangePasswordRequest class validation tests.")
public class ChangePasswordRequestValidationTests {
    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Old password is null
     */
    @Test
    @DisplayName("User old password is null → validation test failed with message: Old password cannot be blank.")
    public void oldPasswordIsNullValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest(null,"1qaZXsw@");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.oldPassword.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("oldPassword")
                && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 2: Old password is blank
     */
    @Test
    @DisplayName("User old password is null → validation test failed with message: Old password cannot be blank.")
    public void oldPasswordIsBlankValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("","1qaZXsw@");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.oldPassword.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("oldPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 3: New password is null
     */
    @Test
    @DisplayName("User new password is null → validation test failed with message: Password cannot be blank.")
    public void newPasswordIsNullValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@",null);
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 4: New password is blank
     */
    @Test
    @DisplayName("User new password is blank → validation test failed with message: Password cannot be blank.")
    public void newPasswordIsBlankValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.notBlank");
        String expectedMessage2 = messages.getString("user.password.length");
        String expectedMessage3 = messages.getString("user.password.validation");
        assertThat(violations).hasSize(3);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expectedMessage2));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expectedMessage3));
    }

    /**
     * Test 5: New password is less than minimum length
     */
    @Test
    @DisplayName("User new password is less than minimum length → validation test failed with message: Password length must be between 8 and 255 characters.")
    public void newPasswordIsMinLengthValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","1qXs@");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 6: New password is more than maximum length
     */
    @Test
    @DisplayName("User new password is more than maximum length → validation test failed with message: Password length must be between 8 and 255 characters.")
    public void newPasswordIsMaxLengthValidationFailedWithMessage() {
        String longPassword = "a".repeat(253 ) + "@1A";
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@",longPassword);
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 7: New password did not have big char
     */
    @Test
    @DisplayName("User new password did not have big char → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void newPasswordWithoutBigCharValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","a1@sdf4g");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 8: New password did not have small char
     */
    @Test
    @DisplayName("User new password did not have small char → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void newPasswordWithoutSmallCharValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","A1@SDF4G");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 9: New password did not have special symbols
     */
    @Test
    @DisplayName("User new password did not have special symbols → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void newPasswordWithoutSpecialSymbolValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","a1BsDf4g");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 10: New password did not have a number
     */
    @Test
    @DisplayName("User new password did not have a number → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void newPasswordWithoutNumberValidationFailedWithMessage() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","a@BsDfcg");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        String expecterMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("newPassword")
                        && violation.getMessage().equals(expecterMessage));
    }

    /**
     * Test 11: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void ChangePasswordRequestIsOk() {
        ChangePasswordRequest dto = new ChangePasswordRequest("1qaZXsw@","P@ssw0rd");
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
