package com.sitool.servicedesk.subcategory.dto.request;

import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation tests for UpdateSubcategoryRequest
 */
@DisplayName("UpdateSubcategoryRequest class validation tests.")
public class UpdateSubcategoryRequestTests {

    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Subcategory name is null
     */
    @Test
    @DisplayName("Subcategory name is null → validation test failed with message: Subcategory name can not be blank.")
    void subcategoryNameIsNullValidationFailedWithMessage() {
        UUID categoryId = UUID.randomUUID();
        UpdateSubcategoryRequest dto = new UpdateSubcategoryRequest(null, "some description", categoryId);
        Set<ConstraintViolation<UpdateSubcategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("subcategory.name.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Subcategory name is blank
     */
    @Test
    @DisplayName("Subcategory name is null → validation test failed with message: Subcategory name can not be blank. / Subcategory name length must be between 2 and 255 characters.")
    void subcategoryNameIsBlankValidationFailedWithMessage() {
        UUID categoryId = UUID.randomUUID();
        UpdateSubcategoryRequest dto = new UpdateSubcategoryRequest("", "some description", categoryId);
        Set<ConstraintViolation<UpdateSubcategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("subcategory.name.notBlank");
        String expectedMessage2 = messages.getString("subcategory.name.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 3: Subcategory name is less then min length
     */
    @Test
    @DisplayName("Subcategory name is less then min length → validation test failed with message: Subcategory name length must be between 2 and 255 characters.")
    void categoryNameIsMinLengthValidationFailedWithMessage() {
        UUID categoryId = UUID.randomUUID();
        UpdateSubcategoryRequest dto = new UpdateSubcategoryRequest("A", "some description", categoryId);
        Set<ConstraintViolation<UpdateSubcategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("subcategory.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: Subcategory name has more than maximum length
     */
    @Test
    @DisplayName("Subcategory name has more than maximum length → validation test failed with message: Subcategory name length must be between 2 and 255 characters.")
    void categoryNameHasMoreThanMaxLengthValidationFailedWithMessage() {
        UUID categoryId = UUID.randomUUID();
        String subcategoryName = "a".repeat(256);
        UpdateSubcategoryRequest dto = new UpdateSubcategoryRequest(subcategoryName, "some description", categoryId);
        Set<ConstraintViolation<UpdateSubcategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("subcategory.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: subcategory name is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    void createCategoryRequestIsOk() {
        UUID categoryId = UUID.randomUUID();
        UpdateSubcategoryRequest request = new UpdateSubcategoryRequest("CPU", "CPU subcategory", categoryId);
        Set<ConstraintViolation<UpdateSubcategoryRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 6: Subcategory categoryId is null
     */
    @Test
    @DisplayName("Subcategory categoryId is null → validation test failed with message: Subcategory must be linked to a category.")
    void subcategoryCategoryIdIsNullValidationFailedWithMessage() {

        UpdateSubcategoryRequest dto = new UpdateSubcategoryRequest("CPU", "some description", null);
        Set<ConstraintViolation<UpdateSubcategoryRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("subcategory.categoryId.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("categoryId")
                        && violation.getMessage().equals(expectedMessage));
    }

}
