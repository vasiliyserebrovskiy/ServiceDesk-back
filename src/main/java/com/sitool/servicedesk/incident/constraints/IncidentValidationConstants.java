package com.sitool.servicedesk.incident.constraints;

/**
 * Validation constraints for incident fields.
 */
public class IncidentValidationConstants {
    private IncidentValidationConstants() {}
    /**
     *  Minimum allowed length for a category name.
     */
    public static final int SHORT_DESCRIPTION_MIN_LENGTH = 10;

    /**
     * Maximum allowed length for a category name.
     */
    public static final int SHORT_DESCRIPTION_MAX_LENGTH = 255;
}
