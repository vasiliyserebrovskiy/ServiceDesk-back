package com.sitool.servicedesk.servicenow.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload sent to the ServiceNow Scripted REST API to create an incident.
 * Field names are mapped explicitly to snake_case, since ServiceNow's
 * contract differs from this application's own camelCase API - the rest
 * of the application's JSON serialization is unaffected.
 */
public record ServiceNowIncidentRequest(
        @JsonProperty("short_description") String shortDescription,
        @JsonProperty("description") String description,
        @JsonProperty("category") String category,
        @JsonProperty("subcategory") String subcategory,
        @JsonProperty("impact") String impact,
        @JsonProperty("urgency") String urgency,
        @JsonProperty("external_number") String externalNumber,
        @JsonProperty("requester_email") String requesterEmail,
        @JsonProperty("assignment_group") String assignmentGroup,
        @JsonProperty("assigned_to_email") String assignedToEmail
) {}
