package com.sitool.servicedesk.ci.dto.request;

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
 * Validation tests for CreateCIRequest
 */
@DisplayName("CreateCIRequest class validation tests.")
public class CreateCIRequestTests {
    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: CI name is null
     */
    @Test
    @DisplayName("CI name is null → validation test failed with message: CI name can not be blank.")
    void ciNameIsNullValidationFailedWithMessage() {
        CreateCIRequest dto = new CreateCIRequest(null, "Core network switch located in server room A", "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.name.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: CI name is blank
     */
    @Test
    @DisplayName("CI name is blank → validation test failed with message: CI name can not be blank. / CI name length must be between 2 and 255 characters.")
    void ciNameIsBlankValidationFailedWithMessage() {
        CreateCIRequest dto = new CreateCIRequest("", "Core network switch located in server room A", "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.name.notBlank");
        String expectedMessage2 = messages.getString("ci.name.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 3: CI name is less than min length
     */
    @Test
    @DisplayName("CI name is less then min length → validation test failed with message: CI name length must be between 2 and 255 characters.")
    void ciNameIsMinLengthValidationFailedWithMessage() {
        CreateCIRequest dto = new CreateCIRequest("A", "Core network switch located in server room A", "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: CI name has more than maximum length
     */
    @Test
    @DisplayName("CI name has more than maximum length → validation test failed with message: CI name length must be between 2 and 255 characters.")
    void ciNameIsMaxLengthValidationFailedWithMessage() {
        String ciName = "a".repeat(256);
        CreateCIRequest dto = new CreateCIRequest(ciName, "Core network switch located in server room A", "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.name.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: CI is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    void createCIRequestIsOk() {
        CreateCIRequest request = new CreateCIRequest("Core-SW-01", "Core network switch located in server room A", "Network Equipment", "Cisco", "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 6: CI type has more than maximum length
     */
    @Test
    @DisplayName("CI type has more than maximum length → validation test failed with message: CI type length must be less than 150 characters.")
    void ciTypeIsMaxLengthValidationFailedWithMessage() {
        String type = "a".repeat(151);
        CreateCIRequest dto = new CreateCIRequest("Core-SW-01", "Core network switch located in server room A", type, "Cisco", "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.type.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("type")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 7: CI manufacturer has more than maximum length
     */
    @Test
    @DisplayName("CI manufacturer has more than maximum length → validation test failed with message: CI manufacturer length must be less than 150 characters.")
    void ciManufacturerIsMaxLengthValidationFailedWithMessage() {
        String manufacturer = "a".repeat(151);
        CreateCIRequest dto = new CreateCIRequest("Core-SW-01", "Core network switch located in server room A", "Network Equipment", manufacturer, "FCW2142L0QK", "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.manufacturer.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("manufacturer")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 8: CI serialNumber has more than maximum length
     */
    @Test
    @DisplayName("CI serialNumber has more than maximum length → validation test failed with message: CI serialNumber length must be less than 150 characters.")
    void ciSerialNumberIsMaxLengthValidationFailedWithMessage() {
        String serialNumber = "a".repeat(151);
        CreateCIRequest dto = new CreateCIRequest("Core-SW-01", "Core network switch located in server room A", "Network Equipment", "Cisco", serialNumber, "Catalyst 9300");
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.serialNumber.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("serialNumber")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 9: CI model has more than maximum length
     */
    @Test
    @DisplayName("CI model has more than maximum length → validation test failed with message: CI model length must be less than 150 characters.")
    void ciModelIsMaxLengthValidationFailedWithMessage() {
        String model = "a".repeat(151);
        CreateCIRequest dto = new CreateCIRequest("Core-SW-01", "Core network switch located in server room A", "Network Equipment", "Cisco", "FCW2142L0QK", model);
        Set<ConstraintViolation<CreateCIRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("ci.model.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("model")
                        && violation.getMessage().equals(expectedMessage));
    }
}
