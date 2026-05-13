package com.sitool.servicedesk.user.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user cannot be found in the system.
 *
 * <p>Used in user lookup and authentication flows.</p>
 *
 * <p>Maps to HTTP 404 NOT FOUND response.</p>
 */
public class UserNotFoundException extends RestApiException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found");
    }
}
