package com.sitool.servicedesk.servicenow.settings.dto.request;

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
 * Validation tests for UpdateServiceNowSettingsRequest
 */
@DisplayName("UpdateServiceNowSettingsRequest class validation tests.")
public class UpdateServiceNowSettingsRequestTests {
    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: ServiceNow endpoint is null
     */
    @Test
    @DisplayName("ServiceNow endpoint is null → validation test failed with message: ServiceNow endpoint can not be blank.")
    void servicenowEndpointIsNullValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest(null, "servicedesk.integration", "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.endpoint.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("endpoint")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: ServiceNow endpoint is blank
     */
    @Test
    @DisplayName("ServiceNow endpoint is blank → validation test failed with message: ServiceNow endpoint can not be blank.")
    void servicenowEndpointIsBlankValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("", "servicedesk.integration", "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.endpoint.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("endpoint")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 3: ServiceNow endpoint is not a valid URL
     */
    @Test
    @DisplayName("ServiceNow endpoint is not a valid URL → validation test failed with message: ServiceNow endpoint must be a valid HTTPS URL.")
    void servicenowEndpointIsNotValidUrlValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("not-a-url", "servicedesk.integration", "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.endpoint.url");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("endpoint")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: ServiceNow endpoint is a well-formed URL but uses http instead of https
     */
    @Test
    @DisplayName("ServiceNow endpoint uses http instead of https → validation test failed with message: ServiceNow endpoint must be a valid HTTPS URL.")
    void servicenowEndpointWrongProtocolValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("http://dev388916.service-now.com", "servicedesk.integration", "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.endpoint.url");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("endpoint")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: ServiceNow endpoint is a valid HTTPS URL
     */
    @Test
    @DisplayName("ServiceNow endpoint is a valid HTTPS URL → validation test passed, no violations on endpoint.")
    void servicenowEndpointIsValidHttpsUrlValidationPassed() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("https://dev388916.service-now.com", "servicedesk.integration", "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        assertThat(violations).noneMatch(violation -> violation.getPropertyPath().toString().equals("endpoint"));
    }

    /**
     * Test 6: ServiceNow username is null
     */
    @Test
    @DisplayName("ServiceNow username is null → validation test failed with message: ServiceNow username can not be blank.")
    void servicenowUsernameIsNullValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("https://dev388916.service-now.com", null, "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.username.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("username")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 7: ServiceNow username is blank
     */
    @Test
    @DisplayName("ServiceNow username is blank → validation test failed with message: ServiceNow username can not be blank.")
    void servicenowUsernameIsBlankValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("https://dev388916.service-now.com", "", "1qaZXsw@");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.username.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("username")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 8: ServiceNow password is null
     */
    @Test
    @DisplayName("ServiceNow password is null → validation test failed with message: ServiceNow password can not be blank.")
    void servicenowPasswordIsNullValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("https://dev388916.service-now.com", "servicedesk.integration", null);
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.password.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 9: ServiceNow password is blank
     */
    @Test
    @DisplayName("ServiceNow password is blank → validation test failed with message: ServiceNow password can not be blank.")
    void servicenowPasswordIsBlankValidationFailedWithMessage() {
        UpdateServiceNowSettingsRequest dto = new UpdateServiceNowSettingsRequest("https://dev388916.service-now.com", "servicedesk.integration", "");
        Set<ConstraintViolation<UpdateServiceNowSettingsRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("servicenow.password.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

}
