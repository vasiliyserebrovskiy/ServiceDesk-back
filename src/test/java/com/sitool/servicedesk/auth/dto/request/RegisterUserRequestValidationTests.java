package com.sitool.servicedesk.auth.dto.request;

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
 * Validation tests for RegisterUserRequest
 */
@DisplayName("RegisterUserRequest class validation tests.")
public class RegisterUserRequestValidationTests {

    private static Validator validator;
    private static ResourceBundle messages;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        messages = ResourceBundle.getBundle("ValidationMessages");
    }

    /**
     * Test 1: Firstname is null
    */
    @Test
    @DisplayName("User firstname is null → validation test failed with message: User firstname can not be blank.")
    public void firstnameIsNullValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest(null, "Serebrovskii", "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.firstname.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("firstname")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 2: Firstname is blank
     */
    @Test
    @DisplayName("User firstname is blank → validation test failed with message: User firstname can not be blank. / Firstname length must be between 2 and 100 characters.")
    public void firstnameIsBlankValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("", "Serebrovskii", "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.firstname.notBlank");
        String expectedMessage2 = messages.getString("user.firstname.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("firstname")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("firstname")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 3: Firstname has less than minimum length
     */
    @Test
    @DisplayName("User firstname has less than minimum length → validation test failed with message: Firstname length must be between 2 and 100 characters.")
    public void firstnameIsMinLengthValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("A", "Serebrovskii", "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.firstname.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("firstname")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 4: Firstname has more than maximum length
     */
    @Test
    @DisplayName("User firstname has more than maximum length → validation test failed with message: Firstname length must be between 2 and 100 characters.")
    public void firstnameIsMaxLengthValidationFailedWithMessage() {
        String longName = "a".repeat(101);
        RegisterUserRequest dto = new RegisterUserRequest(longName, "Serebrovskii", "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.firstname.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("firstname")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 5: Lastname is null
     */
    @Test
    @DisplayName("User lastname is null → validation test failed with message: User lastname can not be blank.")
    public void lastnameIsNullValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", null, "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.lastname.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("lastname")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 6: Lastname is blank
     */
    @Test
    @DisplayName("User lastname is blank → validation test failed with message: User lastname can not be blank. / Lastname length must be between 2 and 100 characters.")
    public void lastnameIsBlankValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "", "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.lastname.notBlank");
        String expectedMessage2 = messages.getString("user.lastname.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("lastname")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("lastname")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 7: Lastname has less than minimum length
     */
    @Test
    @DisplayName("User lastname has less than minimum length → validation test failed with message: Lastname length must be between 2 and 100 characters.")
    public void lastnameIsMinLengthValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "S", "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.lastname.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("lastname")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 8: Lastname has more than maximum length
     */
    @Test
    @DisplayName("User lastname has more than maximum length → validation test failed with message: Firstname length must be between 2 and 100 characters.")
    public void lastnameIsMaxLengthValidationFailedWithMessage() {
        String longName = "a".repeat(101);
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", longName, "vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.lastname.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("lastname")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 9: Email is null
     */
    @Test
    @DisplayName("User email is null → validation test failed with message: Email cannot be blank.")
    public void emailIsNullValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", null, "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 10: Email is blank
     */
    @Test
    @DisplayName("User email is blank → validation test failed with message: Email cannot be blank. / Email length must be between 6 and 255 characters.")
    public void emailIsBlankValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.notBlank");
        String expectedMessage2 = messages.getString("user.email.length");
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage2));
    }

    /**
     * Test 11: Email is less than minimum length
     */
    @Test
    @DisplayName("User email is less than minimum length → validation test failed with message: Email length must be between 7 and 255 characters.")
    public void emailIsMinLengthValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "a@b.ru", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 12: Email is more than maximum length
     */
    @Test
    @DisplayName("User email is more than maximum length → validation test failed with message: Email length must be between 7 and 255 characters.")
    public void emailIsMaxLengthValidationFailedWithMessage() {
        String longEmail = "a".repeat(245 ) + "@domain.com";
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", longEmail, "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 13: Email has two @
     */
    @Test
    @DisplayName("User email has two at → validation test failed with message: Invalid email format: must be a valid email address without leading or trailing dots in the local part, domain labels cannot start with a hyphen, and top-level domain must be 2 to 6 letters long.")
    public void emailHasTwoAtValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 14: Email begin with dot
     */
    @Test
    @DisplayName("User email begin with dot → validation test failed with message: Invalid email format: must be a valid email address without leading or trailing dots in the local part, domain labels cannot start with a hyphen, and top-level domain must be 2 to 6 letters long.")
    public void emailBeginWithDotValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", ".vasiliy@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 15: Email domain start with heap
     */
    @Test
    @DisplayName("User email domain start with heap → validation test failed with message: Invalid email format: must be a valid email address without leading or trailing dots in the local part, domain labels cannot start with a hyphen, and top-level domain must be 2 to 6 letters long.")
    public void emailDomainStartWithHyphenValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@-domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 16: Email top level domain less than 2
     */
    @Test
    @DisplayName("User email top level domain less than 2 → validation test failed with message: Invalid email format: must be a valid email address without leading or trailing dots in the local part, domain labels cannot start with a hyphen, and top-level domain must be 2 to 6 letters long.")
    public void emailTopDomainLessThatTwoHyphenValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.c", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 17: Email top level domain more than 6
     */
    @Test
    @DisplayName("User email top level domain more than 6 → validation test failed with message: Invalid email format: must be a valid email address without leading or trailing dots in the local part, domain labels cannot start with a hyphen, and top-level domain must be 2 to 6 letters long.")
    public void emailTopDomainMoreThatSixHyphenValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.comcomv", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.email.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 18: Password is null
     */
    @Test
    @DisplayName("User password is null → validation test failed with message: Password cannot be blank.")
    public void passwordIsNullValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", null);
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.notBlank");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 19: Password is blank
     */
    @Test
    @DisplayName("User password is blank → validation test failed with message: Password cannot be blank. / Password length must be between 8 and 255 characters. / Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void passwordIsBlankValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", "");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.notBlank");
        String expectedMessage2 = messages.getString("user.password.length");
        String expectedMessage3 = messages.getString("user.password.validation");
        assertThat(violations).hasSize(3);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage2));
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage3));
    }

    /**
     * Test 20: Password is less than minimum length
     */
    @Test
    @DisplayName("User password is less than minimum length → validation test failed with message: Password length must be between 8 and 255 characters.")
    public void passwordIsMinLengthValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", "1qXs@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 21: Password is more than maximum length
     */
    @Test
    @DisplayName("User password is more than maximum length → validation test failed with message: Password length must be between 8 and 255 characters.")
    public void passwordIsMaxLengthValidationFailedWithMessage() {
        String longPassword = "a".repeat(253 ) + "@1A";
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", longPassword);
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.length");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 22: Password did not have big char
     */
    @Test
    @DisplayName("User password did not have big char → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void passwordWithoutBigCharValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", "a1@sdf4g");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 23: Password did not have small char
     */
    @Test
    @DisplayName("User password did not have small char → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void passwordWithoutSmallCharValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", "A1@SDF4G");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 24: Password did not have special symbols
     */
    @Test
    @DisplayName("User password did not have special symbol → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void passwordWithoutSpecialSymbolValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", "a1BsDf4g");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 25: Password did not have a number
     */
    @Test
    @DisplayName("User password did not have a number → validation test failed with message: Password must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    public void passwordWithoutNumberValidationFailedWithMessage() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy@domain.com", "a@BsDfcg");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        String expectedMessage = messages.getString("user.password.validation");
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("password")
                        && violation.getMessage().equals(expectedMessage));
    }

    /**
     * Test 26: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void RegisterUserRequestIsOk() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy.serebr@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 27: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void RegisterUserRequestIsOk2() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy-serebr@domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 28: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void RegisterUserRequestIsOk3() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy-serebr@domain-domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 29: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void RegisterUserRequestIsOk4() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy_serebr.serebr@domain-domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    /**
     * Test 30: All data is ok
     */
    @Test
    @DisplayName("All data is ok → validation test is ok.")
    public void RegisterUserRequestIsOk5() {
        RegisterUserRequest dto = new RegisterUserRequest("Vasilii", "Serebrovskii", "vasiliy123.serebr@123domain-domain.com", "1qaZXsw@");
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

}
