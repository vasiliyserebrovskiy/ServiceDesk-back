package com.sitool.servicedesk.sybcategory.dto.response;

import java.util.UUID;

/**
 * Subcategory response DTO.
 */
public record SubcategoryDto(
        UUID id,
        String name,
        String description,
        UUID categoryId
) {}
