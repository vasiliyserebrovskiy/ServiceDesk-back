package com.sitool.servicedesk.incident.dto.request;

import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation tests for UpdateIncidentRequestTests
 */
@DisplayName("UpdateIncidentRequestTests class validation tests.")
public class UpdateIncidentRequestTests {
    private static Validator validator;
    private static ResourceBundle messages;
    private static final LocalDateTime ACTUAL_START = LocalDateTime.of(2026, 8, 17, 10, 30);
    private static final LocalDateTime ACTUAL_END = LocalDateTime.of(2026, 8, 17, 14, 0);

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Incident requesterId can not be null
     */
    @Test
    @DisplayName("Incident requesterId is null → validation test failed with message: Incident requesterId can not be null.")
    void incidentRequesterIdIsNullValidationFailedWithMessage() {

        LocalDateTime actualStart = LocalDateTime.of(2026, 8, 17, 10, 30);
        LocalDateTime actualEnd = LocalDateTime.of(2026, 8, 17, 14, 0);

        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.requesterId.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("requesterId")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Incident categoryId can not be null
     */
    @Test
    @DisplayName("Incident categoryId is null → validation test failed with message: Incident categoryId can not be null.")
    void incidentCategoryIdIsNullValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.categoryId.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("categoryId")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 3: Incident statusId can not be null
     */
    @Test
    @DisplayName("Incident statusId is null → validation test failed with message: Incident statusId can not be null.")
    void incidentStatusIdIsNullValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.statusId.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("statusId")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: Incident priority can not be null
     */
    @Test
    @DisplayName("Incident priority is null → validation test failed with message: Incident priority can not be null.")
    void incidentPriorityIsNullValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.priority.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("priority")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: Incident impact can not be null
     */
    @Test
    @DisplayName("Incident impact is null → validation test failed with message: Incident impact can not be null.")
    void incidentImpactIsNullValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                null,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.impact.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("impact")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 6: Incident urgency can not be null
     */
    @Test
    @DisplayName("Incident urgency is null → validation test failed with message: Incident urgency can not be null.")
    void incidentUrgencyIsNullValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.urgency.notNull");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("urgency")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 7: Incident short description can not be blank
     */
    @Test
    @DisplayName("Incident shortDescription is null → validation test failed with message: Incident shortDescription can not be blank.")
    void incidentShortDescriptionIsNullValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.shortDescription.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("shortDescription")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 8: Incident short description can not be blank
     */
    @Test
    @DisplayName("Incident shortDescription is blank → validation test failed with message: Incident shortDescription can not be blank. / Incident shortDescription length must be between 10 and 255 characters.")
    void incidentShortDescriptionIsBlankValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.shortDescription.notBlank");
        String expectedMessage2 = messages.getString("incident.shortDescription.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("shortDescription")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("shortDescription")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 9: Incident short description can not be less than 10 characters
     */
    @Test
    @DisplayName("Incident shortDescription can not be less than 10 characters → validation test failed with message: Incident shortDescription length must be between 10 and 255 characters.")
    void incidentShortDescriptionIsLessValidationFailedWithMessage() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.shortDescription.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("shortDescription")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 10: Incident short description can not be more than 255 characters
     */
    @Test
    @DisplayName("Incident shortDescription can not be less than 10 characters → validation test failed with message: Incident shortDescription length must be between 10 and 255 characters.")
    void incidentShortDescriptionIsMoreValidationFailedWithMessage() {
        String shortDescription = "a".repeat(256);
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                shortDescription,
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("incident.shortDescription.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("shortDescription")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 11: Incident is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    void createIncidentRequestIsOk() {
        UpdateIncidentRequest dto = new UpdateIncidentRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test incident",
                "Test description",
                "",
                ACTUAL_START,
                ACTUAL_END
        );
        Set<ConstraintViolation<UpdateIncidentRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
