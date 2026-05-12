package com.sitool.servicedesk.user.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to create a user that already exists in the system.
 *
 * <p>This typically occurs during registration when a user with the same unique
 * identifier (such as email) is already present in the database.</p>
 *
 * <p>Maps to HTTP 409 CONFLICT response.</p>
 */
public class UserAlreadyExistException extends RestApiException {

    public UserAlreadyExistException() {
        super(HttpStatus.CONFLICT, "User already exists");
    }

}
