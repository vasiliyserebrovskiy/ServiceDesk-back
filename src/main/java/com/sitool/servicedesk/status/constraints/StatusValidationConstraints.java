package com.sitool.servicedesk.status.constraints;

/**
 * Validation constraints for status fields.
 */
public class StatusValidationConstraints {
    private StatusValidationConstraints() {};
    /**
     *  Minimum allowed length for a status name.
     */
    public static final int NAME_MIN_LENGTH = 2;

    /**
     * Maximum allowed length for a status name.
     */
    public static final int NAME_MAX_LENGTH = 255;
}
