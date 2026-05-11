package com.sitool.servicedesk.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom email validation annotation.
 *
 * <p>Uses {@link CustomEmailValidator} to validate email format
 * and additionally rejects emails containing consecutive dots.</p>
 */
@Documented
@Constraint(validatedBy = CustomEmailValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomEmail {
    String message() default "Wrong email format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
