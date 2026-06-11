package com.sitool.servicedesk.ci.constraints;

/**
 * Validation constraints for CI fields.
 */
public class CIValidationConstraints {
    private CIValidationConstraints() {}
    /**
     *  Minimum allowed length for a CI name.
     */
    public static final int NAME_MIN_LENGTH = 2;

    /**
     * Maximum allowed length for a CI name.
     */
    public static final int NAME_MAX_LENGTH = 255;

    /**
     * Maximum allowed length for a CI type.
     */
    public static final int TYPE_MAX_LENGTH = 150;

    /**
     * Maximum allowed length for a CI manufacturer.
     */
    public static final int MANUFACTURER_MAX_LENGTH = 150;

    /**
     * Maximum allowed length for a CI serialNumber.
     */
    public static final int SERIAL_NUMBER_MAX_LENGTH = 150;

    /**
     * Maximum allowed length for a CI model.
     */
    public static final int MODEL_MAX_LENGTH = 150;
}
