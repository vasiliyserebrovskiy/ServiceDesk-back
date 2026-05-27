package com.sitool.servicedesk.group.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a group with the same unique constraints already exists.
 */
public class GroupAlreadyExistException extends RestApiException {
    public GroupAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Group already exists");
    }
}
