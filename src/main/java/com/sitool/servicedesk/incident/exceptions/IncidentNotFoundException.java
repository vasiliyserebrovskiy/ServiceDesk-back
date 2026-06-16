package com.sitool.servicedesk.incident.exceptions;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.http.HttpStatus;

public class IncidentNotFoundException extends RestApiException {
    public IncidentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Incident not found");
    }
}
