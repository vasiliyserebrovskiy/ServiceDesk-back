package com.sitool.servicedesk.servicenow.dto.response;

/**
 * Top-level response wrapper returned by the ServiceNow REST API.
 * ServiceNow nests the actual payload under a "result" key.
 */
public record ServiceNowIncidentResponseWrapper(
        ServiceNowIncidentResponse result
) {}