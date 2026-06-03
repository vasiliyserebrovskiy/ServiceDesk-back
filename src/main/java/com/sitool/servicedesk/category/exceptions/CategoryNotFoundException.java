package com.sitool.servicedesk.category.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested category cannot be found.
 */
public class CategoryNotFoundException extends RestApiException {
    public CategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Category not found");
    }
}
