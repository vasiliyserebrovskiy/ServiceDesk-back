package com.sitool.servicedesk.status.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested category cannot be found.
 */
public class StatusNotFoundException extends RestApiException {
    public StatusNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Status not found");
    }
}
