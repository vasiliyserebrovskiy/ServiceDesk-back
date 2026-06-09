package com.sitool.servicedesk.sybcategory.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class SubcategoryAlreadyExistException extends RestApiException {
    public SubcategoryAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Subcategory already exists");
    }
}
