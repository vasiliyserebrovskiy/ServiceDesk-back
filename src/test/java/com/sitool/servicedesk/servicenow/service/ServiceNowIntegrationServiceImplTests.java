package com.sitool.servicedesk.servicenow.service;

import com.sitool.servicedesk.incident.entity.Incident;
import com.sitool.servicedesk.incident.repository.IncidentRepository;
import com.sitool.servicedesk.servicenow.client.ServiceNowClient;
import com.sitool.servicedesk.servicenow.dto.response.ServiceNowIncidentResponse;
import com.sitool.servicedesk.servicenow.mapper.IncidentToServiceNowMapper;
import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import com.sitool.servicedesk.servicenow.settings.repository.ServiceNowSettingsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceNowIntegrationServiceImpl unit tests.")
class ServiceNowIntegrationServiceImplTests {

    @Mock
    private ServiceNowSettingsRepository serviceNowSettingsRepository;
    @Mock
    private IncidentRepository incidentRepository;
    @Mock
    private IncidentToServiceNowMapper mapper;
    @Mock
    private ServiceNowClient serviceNowClient;

    @InjectMocks
    private ServiceNowIntegrationServiceImpl service;

    @Test
    @DisplayName("Async sync → success → updates incident sync fields and saves")
    void syncIncidentToServiceNowAsync_shouldUpdateAndSave_whenSuccessful() {
        UUID incidentId = UUID.randomUUID();
        Incident incident = new Incident();

        ServiceNowSettings settings = new ServiceNowSettings();
        settings.setEndpoint("https://dev388916.service-now.com");
        settings.setUsername("integration.servicedesk");
        settings.setPassword("secret");

        ServiceNowIncidentResponse response = new ServiceNowIncidentResponse(
                true, "sys123", "SDINC0001", null, null
        );

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.of(settings));
        when(mapper.toServiceNowRequest(incident)).thenReturn(null);
        when(serviceNowClient.createIncident(eq(settings), any())).thenReturn(response);

        service.syncIncidentToServiceNowAsync(incidentId);

        assertThat(incident.getServicenowSynced()).isTrue();
        assertThat(incident.getServicenowNumber()).isEqualTo("SDINC0001");
        verify(incidentRepository).save(incident);
    }

    @Test
    @DisplayName("Async sync → ServiceNow rejects the incident → marks as failed and saves")
    void syncIncidentToServiceNowAsync_shouldMarkFailed_whenServiceNowRejects() {
        UUID incidentId = UUID.randomUUID();
        Incident incident = new Incident();

        ServiceNowSettings settings = new ServiceNowSettings();
        settings.setEndpoint("https://dev388916.service-now.com");
        settings.setUsername("integration.servicedesk");
        settings.setPassword("secret");

        ServiceNowIncidentResponse response = new ServiceNowIncidentResponse(
                false, null, null, "Requester not found", null
        );

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.of(settings));
        when(mapper.toServiceNowRequest(incident)).thenReturn(null);
        when(serviceNowClient.createIncident(eq(settings), any())).thenReturn(response);

        service.syncIncidentToServiceNowAsync(incidentId);

        assertThat(incident.getServicenowSynced()).isFalse();
        assertThat(incident.getServicenowNumber()).isNull();
        verify(incidentRepository).save(incident);
    }

    @Test
    @DisplayName("Async sync → incident not found → does nothing")
    void syncIncidentToServiceNowAsync_shouldDoNothing_whenIncidentNotFound() {
        UUID incidentId = UUID.randomUUID();

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.empty());

        service.syncIncidentToServiceNowAsync(incidentId);

        verify(incidentRepository, never()).save(any());
        verifyNoInteractions(serviceNowClient);
    }

    @Test
    @DisplayName("Async sync → settings not configured → marks as failed and saves")
    void syncIncidentToServiceNowAsync_shouldMarkFailed_whenSettingsNotConfigured() {
        UUID incidentId = UUID.randomUUID();
        Incident incident = new Incident();

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.empty());

        service.syncIncidentToServiceNowAsync(incidentId);

        assertThat(incident.getServicenowSynced()).isFalse();
        verify(incidentRepository).save(incident);
        verifyNoInteractions(serviceNowClient);
    }
}