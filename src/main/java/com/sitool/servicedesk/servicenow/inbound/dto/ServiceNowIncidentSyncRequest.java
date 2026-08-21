package com.sitool.servicedesk.servicenow.inbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Inbound payload sent by ServiceNow to synchronize the current state of an incident
 * back into the Service Desk application.
 * <p>
 * Represents a full snapshot of the incident as currently known in ServiceNow, not a delta —
 * every relevant field is sent on each call, with {@code null} for values that have not
 * been set yet on that particular incident (e.g. {@code closeComment} before resolution).
 * <p>
 * The incident is identified by the application's own number in the request path
 * ({@code PUT /api/v1/incidents/{number}/servicenow-sync}), not by any field in this payload.
 *
 * @param shortDescription current short description
 * @param description      current description
 * @param category         category name
 * @param subcategory      subcategory name, may be {@code null}
 * @param impact           impact level as text (e.g. {@code LOW}, {@code MEDIUM}, {@code HIGH}, {@code CRITICAL})
 * @param urgency          urgency level as text, same convention as {@code impact}
 * @param assignmentGroup  assignment group name, resolved by name on this side; {@code null} if not assigned
 * @param assignedToEmail  assignee email, resolved by email on this side; {@code null} if not assigned
 * @param ciSerialNumber   linked configuration item serial number, may be {@code null}
 * @param ciName           linked configuration item name, may be {@code null}
 * @param status           current incident status as text, matching the application's status names; never {@code null}
 * @param closeComment     resolution or rejection comment; {@code null} until the incident is resolved or rejected
 * @param actualStart      timestamp of the first entry into In Progress; {@code null} until work has started
 * @param actualEnd        timestamp of resolution; {@code null} until the incident is resolved
 */
public record ServiceNowIncidentSyncRequest (
    @JsonProperty("short_description")
    String shortDescription,
    @JsonProperty("description")
    String description,
    @JsonProperty("category")
    String category,
    @JsonProperty("subcategory")
    String subcategory,
    @JsonProperty("impact")
    String impact,
    @JsonProperty("urgency")
    String urgency,
    @JsonProperty("assignment_group")
    String assignmentGroup,
    @JsonProperty("assigned_to_email")
    String assignedToEmail,
    @JsonProperty("ci_serial_number")
    String ciSerialNumber,
    @JsonProperty("ci_name")
    String ciName,
    @JsonProperty("status")
    @NotBlank
    String status,
    @JsonProperty("close_comment")
    String closeComment,
    @JsonProperty("actual_start")
    LocalDateTime actualStart,
    @JsonProperty("actual_end")
    LocalDateTime actualEnd
){}
