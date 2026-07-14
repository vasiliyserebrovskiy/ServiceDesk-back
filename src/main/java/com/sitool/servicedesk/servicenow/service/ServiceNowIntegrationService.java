package com.sitool.servicedesk.servicenow.service;

import com.sitool.servicedesk.incident.entity.Incident;

import java.util.UUID;

/**
 * Orchestrates syncing a locally created incident to ServiceNow:
 * fetches current integration settings, maps the incident to the
 * ServiceNow request contract, calls the REST API, and applies the
 * result back onto the incident (sync status, ServiceNow number).
 */
public interface ServiceNowIntegrationService {

    /**
     * Attempts to create the given incident in ServiceNow.
     * Never throws - sync failures are reflected on the incident's
     * sync status fields rather than propagated, so a ServiceNow
     * outage does not block incident creation in this application.
     *
     * @param incident the already-persisted local incident to sync
     */
    void syncIncidentToServiceNow(Incident incident);

    /**
     * Attempts to create the given incident in ServiceNow, asynchronously,
     * on a dedicated thread pool. Returns immediately - the caller does not
     * wait for the ServiceNow call to complete.
     * <p>
     * Takes an incident id rather than the entity itself, since this runs
     * outside the caller's transaction/persistence context on a different
     * thread; the incident is re-fetched fresh inside the async method.
     *
     * @param incidentId the id of the already-persisted local incident to sync
     */
    void syncIncidentToServiceNowAsync(UUID incidentId);
}