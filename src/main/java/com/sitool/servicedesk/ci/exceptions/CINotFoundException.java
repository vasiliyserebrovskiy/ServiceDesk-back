package com.sitool.servicedesk.ci.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested CI cannot be found.
 */
public class CINotFoundException extends RestApiException {
    public CINotFoundException() {
        super(HttpStatus.NOT_FOUND, "Configuration item not found");;
    }
}
