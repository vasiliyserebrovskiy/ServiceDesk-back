package com.sitool.servicedesk.group.dto.request;

import com.sitool.servicedesk.usergroup.entity.UserGroupId;
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
     * Test 1: userGroupId is null
     */
    @Test
    @DisplayName("userGroupId is null → validation test failed with message: UserGroupId can not be null.")
    void userGroupIdIsNullValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        UpdateGroupRequest request = new UpdateGroupRequest(null, "Some name", "Some description", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.userGroupId.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("userGroupId")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Group name is null
     */
    @Test
    @DisplayName("Group name is null → validation test failed with message: Group name can not be blank.")
    void  groupNameIsNullValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID(), UUID.randomUUID());
        UpdateGroupRequest request = new UpdateGroupRequest(userGroupId,null, "Some description.", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.name.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));

    }

    /**
     * Test 3: Group name is blank
     */
    @Test
    @DisplayName("Group name is blank → validation test failed with message: Group name can not be blank. / Group name length must be between 2 and 200 characters.")
    void  groupNameIsBlankValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID(), UUID.randomUUID());
        UpdateGroupRequest request = new UpdateGroupRequest(userGroupId,"", "Some description.", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.name.notBlank");
        String expectedMessage2 = messages.getString("group.name.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 4: Group name has less than minimum length
     */
    @Test
    @DisplayName("Group name has less than minimum length → validation test failed with message: Group name length must be between 2 and 200 characters.")
    void  groupNameHasLessThanMinLengthValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID(), UUID.randomUUID());
        UpdateGroupRequest request = new UpdateGroupRequest(userGroupId,"A", "Some description.", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: Group name has more than maximum length
     */
    @Test
    @DisplayName("Group name has more than maximum length → validation test failed with message: Group name length must be between 2 and 200 characters.")
    void  groupNameHasMoreThanMaxLengthValidationFailedWithMessage() {
        List<UUID> userIds = new ArrayList<>();
        String groupName = "a".repeat(201);
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID(), UUID.randomUUID());
        UpdateGroupRequest request = new UpdateGroupRequest(userGroupId,groupName, "Some description.", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        String expectedMessage = messages.getString("group.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 6: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void UpdateGroupRequestIsOk5() {
        List<UUID> userIds = new ArrayList<>();
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID(), UUID.randomUUID());
        UpdateGroupRequest request = new UpdateGroupRequest(userGroupId, "Some name", "Some description", userIds);
        Set<ConstraintViolation<UpdateGroupRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
