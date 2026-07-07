package com.sitool.servicedesk.servicenow.settings.service;

import com.sitool.servicedesk.servicenow.settings.dto.request.UpdateServiceNowSettingsRequest;
import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;

/**
 * Service for managing ServiceNow integration settings.
 * <p>
 * The application keeps at most one settings record at any given time,
 * so both creating the initial settings and updating existing ones are
 * handled through the same upsert-style operation.
 */
public interface ServiceNowSettingsService {

    /**
     * Creates the ServiceNow settings if none exist yet, or fully replaces
     * the existing ones otherwise.
     *
     * @param request the endpoint, username and password to persist;
     *                the password is encrypted before being stored
     * @return the saved settings, with {@code passwordConfigured} reflecting
     *         whether a password was set, but never the password itself
     */
    ServiceNowSettingsDto updateServiceNowSettings(UpdateServiceNowSettingsRequest request);

    /**
     * Returns the current ServiceNow settings.
     *
     * @return the settings if configured; otherwise an object with empty
     *         fields and {@code passwordConfigured} set to {@code false}
     */
    ServiceNowSettingsDto getServiceNowSettings();
}
