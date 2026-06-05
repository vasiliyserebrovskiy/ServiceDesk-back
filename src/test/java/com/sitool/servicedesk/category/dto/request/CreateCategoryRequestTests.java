package com.sitool.servicedesk.category.dto.request;

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
 * Validation tests for CreateCategoryRequest
 */
@DisplayName("CreateCategoryRequest class validation tests.")
public class CreateCategoryRequestTests {
    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Category name is null
     */
    @Test
    @DisplayName("Category name is null → validation test failed with message: Category name can not be blank.")
    void categoryNameIsNullValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest(null, "some description", true, false,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.name.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Category name is blank
     */
    @Test
    @DisplayName("Category name is null → validation test failed with message: Category name can not be blank. / Category name length must be between 2 and 255 characters.")
    void categoryNameIsBlankValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest("", "some description", true, false,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.name.notBlank");
        String expectedMessage2 = messages.getString("category.name.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 3: Category name is null
     */
    @Test
    @DisplayName("Category name is less then min length → validation test failed with message: Category name length must be between 2 and 255 characters.")
    void categoryNameIsMinLengthValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest("A", "some description", true, false,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: Category name has more than maximum length
     */
    @Test
    @DisplayName("Category name has more than maximum length → validation test failed with message: Category name length must be between 2 and 255 characters.")
    void categoryNameHasMoreThanMaxLengthValidationFailedWithMessage() {
        String categoryName = "a".repeat(256);
        CreateCategoryRequest dto = new CreateCategoryRequest(categoryName, "some description", true, false,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: Category name is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    void createCategoryRequestIsOk() {
        CreateCategoryRequest request = new CreateCategoryRequest("Hardware", "Hardware category for Incidents", true, false,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 6: Category isIncident is null
     */
    @Test
    @DisplayName("Category isIncident is null → validation test failed with message: Category name can not be blank.")
    void categoryIsIncidentIsNullValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest("Hardware", "some description", null, false,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.isIncident.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isIncident")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 7: Category isProblem is null
     */
    @Test
    @DisplayName("Category isProblem is null → validation test failed with message: Category name can not be blank.")
    void categoryIsProblemIsNullValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest("Hardware", "some description", true, null,false,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.isProblem.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isProblem")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 8: Category isRequest is null
     */
    @Test
    @DisplayName("Category isRequest is null → validation test failed with message: Category name can not be blank.")
    void categoryIsRequestIsNullValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest("Hardware", "some description", true, false,null,false);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.isRequest.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isRequest")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 9: Category isChange is null
     */
    @Test
    @DisplayName("Category isChange is null → validation test failed with message: Category name can not be blank.")
    void categoryIsChangeIsNullValidationFailedWithMessage() {
        CreateCategoryRequest dto = new CreateCategoryRequest("Hardware", "some description", true, false,false,null);
        Set<ConstraintViolation<CreateCategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("category.isChange.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("isChange")
                        && violation.getMessage().equals(expectedMessage));
    }
}
