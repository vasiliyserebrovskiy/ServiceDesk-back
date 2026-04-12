package com.sitool.servicedesk.user.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends RestApiException {

    public UserAlreadyExistException() {
        super(HttpStatus.CONFLICT, "User already exists");
    }

}
