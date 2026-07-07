package com.sitool.servicedesk.servicenow.settings.service;

import com.sitool.servicedesk.servicenow.settings.dto.request.UpdateServiceNowSettingsRequest;
import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;
import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import com.sitool.servicedesk.servicenow.settings.mapper.ServiceNowSettingsMapper;
import com.sitool.servicedesk.servicenow.settings.repository.ServiceNowSettingsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceNowSettingsServiceImpl unit tests.")
class ServiceNowSettingsServiceImplTests {

    @Mock
    private ServiceNowSettingsRepository serviceNowSettingsRepository;

    @Mock
    private ServiceNowSettingsMapper mapper;

    @InjectMocks
    private ServiceNowSettingsServiceImpl serviceNowSettingsService;

    @Captor
    private ArgumentCaptor<ServiceNowSettings> settingsCaptor;

    @Test
    @DisplayName("Should create new settings when none exist yet")
    void updateServiceNowSettings_shouldCreateNew_whenNoneExist() {
        UpdateServiceNowSettingsRequest request = new UpdateServiceNowSettingsRequest(
                "https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents",
                "integration.servicedesk",
                "SomeStrongPassword1!"
        );

        ServiceNowSettings saved = new ServiceNowSettings();
        ServiceNowSettingsDto expectedDto = new ServiceNowSettingsDto(
                UUID.randomUUID(), request.endpoint(), request.username(), true
        );

        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.empty());
        when(serviceNowSettingsRepository.save(any(ServiceNowSettings.class))).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(expectedDto);

        ServiceNowSettingsDto result = serviceNowSettingsService.updateServiceNowSettings(request);

        verify(serviceNowSettingsRepository).save(settingsCaptor.capture());
        ServiceNowSettings captured = settingsCaptor.getValue();

        assertThat(captured.getEndpoint()).isEqualTo(request.endpoint());
        assertThat(captured.getUsername()).isEqualTo(request.username());
        assertThat(captured.getPassword()).isEqualTo(request.password());
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Should overwrite existing settings")
    void updateServiceNowSettings_shouldOverwriteExisting_whenSettingsAlreadyExist() {
        ServiceNowSettings existing = new ServiceNowSettings();
        existing.setEndpoint("https://old-instance.service-now.com");
        existing.setUsername("old.integration");
        existing.setPassword("OldPassword1!");

        UpdateServiceNowSettingsRequest request = new UpdateServiceNowSettingsRequest(
                "https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents",
                "integration.servicedesk",
                "NewStrongPassword1!"
        );

        ServiceNowSettingsDto expectedDto = new ServiceNowSettingsDto(
                UUID.randomUUID(), request.endpoint(), request.username(), true
        );

        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.of(existing));
        when(serviceNowSettingsRepository.save(existing)).thenReturn(existing);
        when(mapper.toDto(existing)).thenReturn(expectedDto);

        ServiceNowSettingsDto result = serviceNowSettingsService.updateServiceNowSettings(request);

        assertThat(existing.getEndpoint()).isEqualTo(request.endpoint());
        assertThat(existing.getUsername()).isEqualTo(request.username());
        assertThat(existing.getPassword()).isEqualTo(request.password());
        assertThat(result).isEqualTo(expectedDto);

        verify(serviceNowSettingsRepository, never()).save(argThat(s -> s != existing));
    }

    @Test
    @DisplayName("Should return settings when they exist")
    void getServiceNowSettings_shouldReturnSettings_whenTheyExist() {
        ServiceNowSettings existing = new ServiceNowSettings();
        existing.setEndpoint("https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents");
        existing.setUsername("integration.servicedesk");
        existing.setPassword("SomeStrongPassword1!");

        ServiceNowSettingsDto expectedDto = new ServiceNowSettingsDto(
                UUID.randomUUID(), existing.getEndpoint(), existing.getUsername(), true
        );

        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.of(existing));
        when(mapper.toDto(existing)).thenReturn(expectedDto);

        ServiceNowSettingsDto result = serviceNowSettingsService.getServiceNowSettings();

        assertThat(result).isEqualTo(expectedDto);
        verify(serviceNowSettingsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return empty-shaped settings when none exist")
    void getServiceNowSettings_shouldReturnEmptyShape_whenNoneExist() {
        ServiceNowSettingsDto emptyDto = new ServiceNowSettingsDto(null, null, null, false);

        when(serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.empty());
        when(mapper.toDto(any(ServiceNowSettings.class))).thenReturn(emptyDto);

        ServiceNowSettingsDto result = serviceNowSettingsService.getServiceNowSettings();

        assertThat(result).isEqualTo(emptyDto);
        verify(serviceNowSettingsRepository, never()).save(any());
    }
}
