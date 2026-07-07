package com.sitool.servicedesk.servicenow.settings.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

/**
 * Request payload for updating ServiceNow settings.
 */
public record UpdateServiceNowSettingsRequest(
        @NotBlank(message = "{servicenow.endpoint.notBlank}")
        @URL(protocol = "https", message = "{servicenow.endpoint.url}")
        String endpoint,
        @NotBlank(message = "{servicenow.username.notBlank}")
        String username,
        @NotBlank(message = "{servicenow.password.notBlank}")
        String password
) {}
