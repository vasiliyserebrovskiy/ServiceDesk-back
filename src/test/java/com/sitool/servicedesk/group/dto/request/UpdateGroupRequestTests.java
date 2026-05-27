package com.sitool.servicedesk.group.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation tests for GroupUpdateRequest
 */
@DisplayName("GroupUpdateRequest class validation tests.")
public class UpdateGroupRequestTests {
    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Group name has less than minimum length
     */
    @Test
    @DisplayName("Group name has less than minimum length → validation test failed with message: Group name length must be between 2 and 200 characters.")
    void  groupNameHasLessThanMinLengthValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        UpdateGroupRequest request = new UpdateGroupRequest("A", "Some description.", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Group name has more than maximum length
     */
    @Test
    @DisplayName("Group name has more than maximum length → validation test failed with message: Group name length must be between 2 and 200 characters.")
    void  groupNameHasMoreThanMaxLengthValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        String groupName = "a".repeat(201);
        UpdateGroupRequest request = new UpdateGroupRequest(groupName, "Some description.", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 3: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void UpdateGroupRequestIsOk5() {
        List<UUID> userIds = new ArrayList<>();
        UpdateGroupRequest request = new UpdateGroupRequest("Some name", "Some description", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
