package com.sitool.servicedesk.group.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class GroupNotFoundException extends RestApiException {
    public GroupNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Group not found");
    }
}

