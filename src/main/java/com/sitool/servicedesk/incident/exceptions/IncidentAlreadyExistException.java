package com.sitool.servicedesk.incident.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class IncidentAlreadyExistException extends RestApiException {
    public IncidentAlreadyExistException() {
        super(HttpStatus.CONFLICT, "Incident already exists");
    }
}
