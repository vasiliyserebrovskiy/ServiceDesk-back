package com.sitool.servicedesk.group.constraints;

/**
 * Validation constraints for group fields.
 */
public final class GroupValidationConstants {
    private GroupValidationConstants() {}

    /**
     *  Minimum allowed length for a group name.
     */
    public static final int NAME_MIN_LENGTH = 2;

    /**
     * Maximum allowed length for a group name.
     */
    public static final int NAME_MAX_LENGTH = 200;
}
