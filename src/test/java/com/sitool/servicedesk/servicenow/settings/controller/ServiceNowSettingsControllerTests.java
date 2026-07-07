package com.sitool.servicedesk.servicenow.settings.controller;


import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.servicenow.settings.dto.request.UpdateServiceNowSettingsRequest;
import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;
import com.sitool.servicedesk.servicenow.settings.service.ServiceNowSettingsService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceNowSettingsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ServiceNowSettingsControllerTests.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
public class ServiceNowSettingsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceNowSettingsService serviceNowSettingsService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ServiceNowSettingsService serviceNowSettingsService() {
            return mock(ServiceNowSettingsService.class);
        }

        @Bean
        JwtTokenService jwtTokenService() {
            return mock(JwtTokenService.class);
        }

        @Bean
        CustomUserDetailsService customUserDetailsService() {
            return mock(CustomUserDetailsService.class);
        }

        @Bean
        RefreshTokenService refreshTokenService() {
            return mock(RefreshTokenService.class);
        }
    }

    @Test
    @WithMockUser
    @DisplayName("Should update ServiceNow settings and return 200")
    void updateServiceNowSettings_shouldReturnUpdate() throws Exception {
        UUID settingsId = UUID.randomUUID();

        ServiceNowSettingsDto response = new ServiceNowSettingsDto(
                settingsId,
                "https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents",
                "integration.servicedesk",
                true
        );

        when(serviceNowSettingsService.updateServiceNowSettings(any(UpdateServiceNowSettingsRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/servicenow/settings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                  "endpoint": "https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents",
                                  "username": "integration.servicedesk",
                                  "password": "SomeStrongPassword1!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(settingsId.toString()))
                .andExpect(jsonPath("$.username").value("integration.servicedesk"))
                .andExpect(jsonPath("$.passwordConfigured").value(true));

        verify(serviceNowSettingsService).updateServiceNowSettings(any(UpdateServiceNowSettingsRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when update request is invalid")
    void updateServiceNowSettings_shouldReturnBadRequest_whenInvalidInput() throws Exception {

        mockMvc.perform(put("/api/v1/servicenow/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "endpoint": "dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents",
                                  "username": "integration.servicedesk",
                                  "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(serviceNowSettingsService, never()).updateServiceNowSettings(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return ServiceNow settings")
    void getServiceNowSettings_shouldReturnServiceNowSettings() throws Exception {
        UUID settingsId = UUID.randomUUID();
        ServiceNowSettingsDto response = new ServiceNowSettingsDto(
                settingsId,
                "https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents",
                "integration.servicedesk",
                true
        );

        when(serviceNowSettingsService.getServiceNowSettings()).thenReturn(response);

        mockMvc.perform(get("/api/v1/servicenow/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(settingsId.toString()))
                .andExpect(jsonPath("$.endpoint").value("https://dev388916.service-now.com/api/x_1952794_servic_0/servicedesk_rest_integration/incidents"))
                .andExpect(jsonPath("$.username").value("integration.servicedesk"))
                .andExpect(jsonPath("$.passwordConfigured").value(true));

        verify(serviceNowSettingsService).getServiceNowSettings();
    }
}
