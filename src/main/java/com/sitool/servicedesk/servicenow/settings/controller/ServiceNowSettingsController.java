package com.sitool.servicedesk.servicenow.settings.controller;

import com.sitool.servicedesk.servicenow.settings.dto.request.UpdateServiceNowSettingsRequest;
import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;
import com.sitool.servicedesk.servicenow.settings.service.ServiceNowSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementation for ServiceNow Settings operations.
 */
@RestController
@RequiredArgsConstructor
public class ServiceNowSettingsController implements ServiceNowSettingsApi{

    private final ServiceNowSettingsService serviceNowSettingsService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ServiceNowSettingsDto updateServiceNowSettings(UpdateServiceNowSettingsRequest request) {
        return serviceNowSettingsService.updateServiceNowSettings(request);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ServiceNowSettingsDto getServiceNowSettings() {
        return serviceNowSettingsService.getServiceNowSettings();
    }
}
