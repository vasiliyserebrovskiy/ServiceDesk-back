package com.sitool.servicedesk.role.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class RoleNotExistException extends RestApiException {
    public RoleNotExistException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Role did not found");
    }
}
