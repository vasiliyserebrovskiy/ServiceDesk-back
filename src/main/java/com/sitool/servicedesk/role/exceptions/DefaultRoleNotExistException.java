package com.sitool.servicedesk.role.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Thrown when the system cannot find a default role.
 */
public class DefaultRoleNotExistException extends RestApiException {
    public DefaultRoleNotExistException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Default role did not found");
    }
}
