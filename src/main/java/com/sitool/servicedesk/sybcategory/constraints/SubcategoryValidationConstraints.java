package com.sitool.servicedesk.sybcategory.constraints;

/**
 * Validation constraints for subcategory fields.
 */
public class SubcategoryValidationConstraints {
    private SubcategoryValidationConstraints() {}
    /**
     *  Minimum allowed length for a subcategory name.
     */
    public static final int NAME_MIN_LENGTH = 2;

    /**
     * Maximum allowed length for a subcategory name.
     */
    public static final int NAME_MAX_LENGTH = 255;
}
