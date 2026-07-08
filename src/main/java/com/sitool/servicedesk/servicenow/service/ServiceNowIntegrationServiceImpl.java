package com.sitool.servicedesk.servicenow.service;

import com.sitool.servicedesk.incident.entity.Incident;
import com.sitool.servicedesk.servicenow.client.ServiceNowClient;
import com.sitool.servicedesk.servicenow.dto.request.ServiceNowIncidentRequest;
import com.sitool.servicedesk.servicenow.dto.response.ServiceNowIncidentResponse;
import com.sitool.servicedesk.servicenow.exceptions.ServiceNowIntegrationException;
import com.sitool.servicedesk.servicenow.mapper.IncidentToServiceNowMapper;
import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import com.sitool.servicedesk.servicenow.settings.repository.ServiceNowSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of {@link ServiceNowIntegrationService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceNowIntegrationServiceImpl implements ServiceNowIntegrationService {

    private final ServiceNowSettingsRepository serviceNowSettingsRepository;
    private final IncidentToServiceNowMapper mapper;
    private final ServiceNowClient serviceNowClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public void syncIncidentToServiceNow(Incident incident) {
        ServiceNowSettings settings = serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElse(null);

        if (settings == null || settings.getEndpoint() == null) {
            log.warn("ServiceNow integration is not configured, skipping sync for incident {}",
                    incident.getNumber());
            markAsFailed(incident);
            return;
        }

        try {
            ServiceNowIncidentRequest request = mapper.toServiceNowRequest(incident);
            ServiceNowIncidentResponse response = serviceNowClient.createIncident(settings, request);

            if (response.success()) {
                incident.setServicenowNumber(response.number());
                incident.setServicenowSynced(true);
                incident.setServicenowSyncedAt(LocalDateTime.now());

                if (response.warnings() != null && !response.warnings().isEmpty()) {
                    log.warn("ServiceNow sync for incident {} completed with warnings: {}",
                            incident.getNumber(), response.warnings());
                }
            } else {
                log.error("ServiceNow rejected incident {}: {}", incident.getNumber(), response.error());
                markAsFailed(incident);
            }

        } catch (ServiceNowIntegrationException ex) {
            log.error("Failed to sync incident {} to ServiceNow", incident.getNumber(), ex);
            markAsFailed(incident);
        }
    }

    private void markAsFailed(Incident incident) {
        incident.setServicenowSynced(false);
    }
}