package com.sitool.servicedesk.category.dto.request;


import com.sitool.servicedesk.category.constraints.CategoryValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for creating a category.
 */
public record CreateCategoryRequest(
        @NotBlank(message = "{category.name.notBlank}")
        @Size(
                min = CategoryValidationConstants.NAME_MIN_LENGTH,
                max = CategoryValidationConstants.NAME_MAX_LENGTH,
                message="{category.name.length}"
        )
        String name,
        String description,
        boolean isIncident,
        boolean isProblem,
        boolean isRequest,
        boolean isChange
) {}
