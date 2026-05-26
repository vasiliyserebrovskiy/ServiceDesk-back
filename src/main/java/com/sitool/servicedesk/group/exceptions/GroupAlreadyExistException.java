package com.sitool.servicedesk.group.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class GroupAlreadyExistException extends RestApiException {
    public GroupAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Group already exists");
    }
}
