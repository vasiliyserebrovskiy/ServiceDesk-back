package com.sitool.servicedesk.user.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user attempts to change another user's password.
 *
 * <p>Raised during password change operation if the authenticated user's ID
 * does not match the target user ID. Returns HTTP 403 Forbidden.</p>
 */
public class PasswordChangeNotAllowedException extends RestApiException {
    public PasswordChangeNotAllowedException() {
        super(HttpStatus.FORBIDDEN, "Cannot change another user's password");
    }
}
