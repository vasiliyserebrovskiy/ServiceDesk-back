package com.sitool.servicedesk.category.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a category with the same unique constraints already exists.
 */
public class CategoryAlreadyExistException extends RestApiException {
    public CategoryAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Category already exists");
    }
}
