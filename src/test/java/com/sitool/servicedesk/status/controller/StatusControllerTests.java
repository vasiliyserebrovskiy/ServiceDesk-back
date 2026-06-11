package com.sitool.servicedesk.status.controller;

import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.service.StatusService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatusController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(StatusControllerTests.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
public class StatusControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusService statusService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        StatusService statusService() {return mock(StatusService.class);}

        @Bean
        JwtTokenService jwtTokenService() {return mock(JwtTokenService.class);}

        @Bean
        CustomUserDetailsService customUserDetailsService() {return mock(CustomUserDetailsService.class);}

        @Bean
        RefreshTokenService refreshTokenService() {return mock(RefreshTokenService.class);}
    }

    @Test
    @WithMockUser
    @DisplayName("Should create status and return 201")
    void createStatus_shouldReturnCreated() throws Exception {

        StatusDto response = new StatusDto(
                UUID.randomUUID(),
                "Open",
                "Some description",
                true,
                false,
                false,
                false,
                false
        );

        when(statusService.createStatus(any(CreateStatusRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Open",
                                "description":"Some description",
                                "isIncident": true,
                                "isProblem": false,
                                "isRequest": false,
                                "isChange": false,
                                "isTask": false
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Open"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.isIncident").value(true))
                .andExpect(jsonPath("$.isProblem").value(false))
                .andExpect(jsonPath("$.isRequest").value(false))
                .andExpect(jsonPath("$.isChange").value(false))
                .andExpect(jsonPath("$.isTask").value(false));

        verify(statusService).createStatus(any(CreateStatusRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when create request is invalid")
    void createStatus_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "description":"Some description"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        verify(statusService, never()).createStatus(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update status and return 200")
    void updateStatus_shouldReturnUpdatedCategory() throws Exception {
        UUID statusId = UUID.randomUUID();

        StatusDto response = new StatusDto(
                statusId,
                "Updated Status",
                "Updated Description",
                true,
                false,
                false,
                false,
                false
        );

        when(statusService.updateStatus(any(UUID.class), any(UpdateStatusRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/statuses/{categoryId}", statusId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Updated Status",
                          "description":"Updated Description",
                          "isIncident": true,
                          "isProblem": false,
                          "isRequest": false,
                          "isChange": false,
                          "isTask": false
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statusId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Status"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(statusService).updateStatus(any(UUID.class), any(UpdateStatusRequest.class));

    }

    @Test
    @WithMockUser
    @DisplayName("Should delete status and return 204")
    void deleteStatus_shouldReturnNoContent() throws Exception {

        UUID statusId = UUID.randomUUID();

        doNothing().when(statusService).deleteStatus(statusId);

        mockMvc.perform(delete("/api/v1/statuses/{statusId}", statusId))
                .andExpect(status().isNoContent());

        verify(statusService).deleteStatus(statusId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return status by id")
    void getStatus_shouldReturnCategory() throws Exception {
        UUID statusId = UUID.randomUUID();

        StatusDto response = new StatusDto(
                statusId,
                "Open",
                "Some description",
                true,
                false,
                false,
                false,
                false
        );

        when(statusService.getStatus(statusId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/statuses/{statusId}", statusId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statusId.toString()))
                .andExpect(jsonPath("$.name").value("Open"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.isIncident").value(true))
                .andExpect(jsonPath("$.isProblem").value(false))
                .andExpect(jsonPath("$.isRequest").value(false))
                .andExpect(jsonPath("$.isChange").value(false))
                .andExpect(jsonPath("$.isTask").value(false));

        verify(statusService).getStatus(statusId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return all incident statuses")
    void getAllIncidentStatuses_shouldReturnAllIncidentCategories() throws Exception {
        StatusDto first = new StatusDto(
                UUID.randomUUID(),
                "Open",
                "First description",
                true,
                false,
                false,
                false,
                false
        );

        StatusDto second = new StatusDto(
                UUID.randomUUID(),
                "In Progress",
                "Second description",
                true,
                false,
                false,
                false,
                false
        );

        when(statusService.getAllStatuses("INCIDENT"))
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/statuses?type=INCIDENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Open"))
                .andExpect(jsonPath("$[1].name").value("In Progress"));

        verify(statusService).getAllStatuses("INCIDENT");
    }

}
