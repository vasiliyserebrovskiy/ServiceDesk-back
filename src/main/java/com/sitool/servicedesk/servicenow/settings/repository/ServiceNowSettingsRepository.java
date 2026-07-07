package com.sitool.servicedesk.servicenow.settings.repository;

import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link ServiceNowSettings} entities.
 * Provides query methods for ServiceNow Settings persistence.
 */

public interface ServiceNowSettingsRepository extends JpaRepository<ServiceNowSettings, UUID> {
    Optional<ServiceNowSettings> findFirstByOrderByCreatedAtAsc();
}
