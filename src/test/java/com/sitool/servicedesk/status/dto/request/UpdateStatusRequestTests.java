package com.sitool.servicedesk.status.dto.request;

import org.junit.jupiter.api.DisplayName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation tests for UpdateStatusRequest
 */
@DisplayName("UpdateStatusRequest class validation tests.")
public class UpdateStatusRequestTests {
    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Status name is null
     */
    @Test
    @DisplayName("Status name is null → validation test failed with message: Status name can not be blank.")
    void statusNameIsNullValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest(null, "some description", true, false,false,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.name.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Status name is blank
     */
    @Test
    @DisplayName("Status name is blank → validation test failed with message: Status name can not be blank. / Status name length must be between 2 and 255 characters.")
    void statusNameIsBlankValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("", "some description", true, false,false,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.name.notBlank");
        String expectedMessage2 = messages.getString("status.name.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 3: Status name is less than min length
     */
    @Test
    @DisplayName("Status name is less then min length → validation test failed with message: Status name length must be between 2 and 255 characters.")
    void statusNameIsMinLengthValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("A", "some description", true, false,false,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: Status name has more than maximum length
     */
    @Test
    @DisplayName("Status name has more than maximum length → validation test failed with message: Status name length must be between 2 and 255 characters.")
    void statusNameHasMoreThanMaxLengthValidationFailedWithMessage() {
        String categoryName = "a".repeat(256);
        UpdateStatusRequest dto = new UpdateStatusRequest(categoryName, "some description", true, false,false,false,false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: Status is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    void createStatusRequestIsOk() {
        UpdateStatusRequest request = new UpdateStatusRequest("Open", "Hardware category for Incidents", true, false,false,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 6: Status isIncident is null
     */
    @Test
    @DisplayName("Status isIncident is null → validation test failed with message: Status must specify whether it applies to incidents.")
    void statusIsIncidentIsNullValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("Open", "some description", null, false,false,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.isIncident.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isIncident")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 7: Status isProblem is null
     */
    @Test
    @DisplayName("Status isProblem is null → validation test failed with message: Status must specify whether it applies to problem.")
    void statusIsProblemIsNullValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("Open", "some description", false, null,false,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.isProblem.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isProblem")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 8: Status isRequest is null
     */
    @Test
    @DisplayName("Status isRequest is null → validation test failed with message: Status must specify whether it applies to request.")
    void statusIsRequestIsNullValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("Open", "some description", false, false,null,false, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.isRequest.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isRequest")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 9: Status isChange is null
     */
    @Test
    @DisplayName("Status isChange is null → validation test failed with message: Status must specify whether it applies to change order.")
    void statusIsChangeIsNullValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("Open", "some description", false, false,false,null, false);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.isChange.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isChange")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 10: Status isTask is null
     */
    @Test
    @DisplayName("Status isTask is null → validation test failed with message: Status must specify whether it applies to task.")
    void statusIsTaskIsNullValidationFailedWithMessage() {
        UpdateStatusRequest dto = new UpdateStatusRequest("Open", "some description", false, false,false,false, null);
        Set<ConstraintViolation<UpdateStatusRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("status.isTask.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isTask")
                        && violation.getMessage().equals(expectedMessage));
    }
}
