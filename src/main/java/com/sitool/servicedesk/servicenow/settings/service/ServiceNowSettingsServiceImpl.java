package com.sitool.servicedesk.servicenow.settings.service;

import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import com.sitool.servicedesk.servicenow.settings.dto.request.UpdateServiceNowSettingsRequest;
import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;
import com.sitool.servicedesk.servicenow.settings.mapper.ServiceNowSettingsMapper;
import com.sitool.servicedesk.servicenow.settings.repository.ServiceNowSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ServiceNowSettingsService} for managing ServiceNow Settings.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceNowSettingsServiceImpl implements ServiceNowSettingsService {

    private final ServiceNowSettingsRepository serviceNowSettingsRepository;
    private final ServiceNowSettingsMapper mapper;

    /**
     * <p>Looks up the existing settings record, or creates a new one if none
     * exists yet, then overwrites its fields with the request values.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceNowSettingsDto updateServiceNowSettings(UpdateServiceNowSettingsRequest request) {
        ServiceNowSettings settings = serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(ServiceNowSettings::new);

        settings.setEndpoint(request.endpoint());
        settings.setUsername(request.username());
        settings.setPassword(request.password());

        ServiceNowSettings saved = serviceNowSettingsRepository.save(settings);

        return mapper.toDto(saved);
    }

    /**
     * <p>Falls back to an empty, unsaved {@link ServiceNowSettings} instance
     * if no settings record exists yet, so the mapped response always has
     * a predictable shape.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ServiceNowSettingsDto getServiceNowSettings() {

        ServiceNowSettings settings = serviceNowSettingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(ServiceNowSettings::new);

        return mapper.toDto(settings);
    }
}
