package com.sitool.servicedesk.user.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the provided current password does not match
 * the user's actual password during a password change operation.
 *
 * <p>Raised in password change flow when the user provides an incorrect
 * current password. Returns HTTP 400 Bad Request.</p>
 */
public class InvalidPasswordException extends RestApiException {
    public InvalidPasswordException() {
        super(HttpStatus.BAD_REQUEST, "Current password is incorrect.");
    }
}
