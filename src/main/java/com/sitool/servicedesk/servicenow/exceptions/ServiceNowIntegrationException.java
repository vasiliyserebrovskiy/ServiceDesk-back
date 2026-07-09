package com.sitool.servicedesk.servicenow.exceptions;

public class ServiceNowIntegrationException extends RuntimeException {
    public ServiceNowIntegrationException(String message) {
        super(message);
    }

    public ServiceNowIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
