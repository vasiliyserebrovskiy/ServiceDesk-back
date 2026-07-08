package com.sitool.servicedesk.servicenow.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The actual incident creation result, nested under "result" in the response.
 */
public record ServiceNowIncidentResponse(
        boolean success,
        @JsonProperty("sys_id") String sysId,
        String number,
        String error,
        List<String> warnings
) {}