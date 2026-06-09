package com.sitool.servicedesk.sybcategory.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class SubcategoryNotFoundException extends RestApiException {
    public SubcategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Subcategory not found");
    }
}
