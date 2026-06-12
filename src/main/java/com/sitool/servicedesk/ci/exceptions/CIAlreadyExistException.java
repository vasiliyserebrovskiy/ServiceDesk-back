package com.sitool.servicedesk.ci.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a CI with the same unique constraints already exists.
 */
public class CIAlreadyExistException extends RestApiException {
    public CIAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Configuration item already exists");
    }
}
