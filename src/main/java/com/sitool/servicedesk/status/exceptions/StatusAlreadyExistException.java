package com.sitool.servicedesk.status.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a status with the same unique constraints already exists.
 */
public class StatusAlreadyExistException extends RestApiException {
    public StatusAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Status already exists");
    }
}
