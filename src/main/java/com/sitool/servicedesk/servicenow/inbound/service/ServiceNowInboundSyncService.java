package com.sitool.servicedesk.servicenow.inbound.service;

import com.sitool.servicedesk.servicenow.inbound.dto.ServiceNowIncidentSyncRequest;
import com.sitool.servicedesk.incident.exceptions.IncidentNotFoundException;

/**
 * Applies incident state received from ServiceNow back onto the corresponding
 * incident in the Service Desk application.
 * <p>
 * The incident is looked up by its application-level {@code number}, not by any
 * identifier in the request body.
 */
public interface ServiceNowInboundSyncService {

    /**
     * Updates the incident identified by {@code number} with the full state snapshot
     * received from ServiceNow.
     * <p>
     * Best-effort semantics: if the incident is found, every field in {@code request}
     * is applied where possible. A field that cannot be resolved or applied
     * (e.g. {@code assignmentGroup}/{@code assignedToEmail} not matching an existing
     * record) is logged and left untouched, rather than failing the whole update.
     *
     * @param number  the application's own incident number (not the ServiceNow number)
     * @param request full current state of the incident as known in ServiceNow
     * @throws IncidentNotFoundException
     *         if no incident with the given {@code number} exists
     */
    void syncIncidentUpdate(String number, ServiceNowIncidentSyncRequest request);
}