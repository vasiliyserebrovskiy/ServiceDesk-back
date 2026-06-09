package com.sitool.servicedesk.sybcategory.dto.request;

import com.sitool.servicedesk.sybcategory.constraints.SubcategoryValidationConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request payload for creating a subcategory.
 */
public record CreateSubcategoryRequest(
        @NotBlank(message = "{subcategory.name.notBlank}")
        @Size(
                min = SubcategoryValidationConstraints.NAME_MIN_LENGTH,
                max = SubcategoryValidationConstraints.NAME_MAX_LENGTH,
                message = "{subcategory.name.length}"
        )
        String name,
        String description,
        @NotNull(message = "{subcategory.categoryId.notNull}")
        UUID categoryId
) {
}
