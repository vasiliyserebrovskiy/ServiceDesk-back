package com.sitool.servicedesk.category.constraints;

/**
 * Validation constraints for category fields.
 */
public class CategoryValidationConstants {
    private CategoryValidationConstants() {}

    /**
     *  Minimum allowed length for a category name.
     */
    public static final int NAME_MIN_LENGTH = 2;

    /**
     * Maximum allowed length for a category name.
     */
    public static final int NAME_MAX_LENGTH = 255;
}
