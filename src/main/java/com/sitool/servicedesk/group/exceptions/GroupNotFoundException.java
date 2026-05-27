package com.sitool.servicedesk.group.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested group cannot be found.
 */
public class GroupNotFoundException extends RestApiException {
    public GroupNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Group not found");
    }
}

