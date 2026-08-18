package com.sitool.servicedesk.incident.controller;


import com.sitool.servicedesk.incident.dto.request.CreateIncidentRequest;
import com.sitool.servicedesk.incident.dto.request.UpdateIncidentRequest;
import com.sitool.servicedesk.incident.dto.response.IncidentDto;
import com.sitool.servicedesk.incident.dto.response.NextIncidentNumberResponse;
import com.sitool.servicedesk.incident.service.IncidentService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(IncidentControllerTests.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
public class IncidentControllerTests {

    private static final LocalDateTime ACTUAL_START = LocalDateTime.of(2026, 8, 17, 10, 30);
    private static final LocalDateTime ACTUAL_END = LocalDateTime.of(2026, 8, 17, 14, 0);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IncidentService incidentService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        IncidentService incidentService() {return mock(IncidentService.class);}

        @Bean
        JwtTokenService jwtTokenService() {return mock(JwtTokenService.class);}

        @Bean
        CustomUserDetailsService customUserDetailsService() {return mock(CustomUserDetailsService.class);}

        @Bean
        RefreshTokenService refreshTokenService() {return mock(RefreshTokenService.class);}
    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(incidentService);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return incident number")
    void getIncidentNumber() throws Exception {
        NextIncidentNumberResponse incidentNumber = new NextIncidentNumberResponse("INC0000001");

        when(incidentService.getNextIncidentNumber()).thenReturn(incidentNumber);

        mockMvc.perform(get("/api/v1/incidents/next-number"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("INC0000001"));
        verify(incidentService).getNextIncidentNumber();
    }

    @Test
    @WithMockUser
    @DisplayName("Should create incident and return 201")
    void createIncident_shouldReturnCreated() throws Exception {
        UUID incidentId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto incident = new IncidentDto(
                incidentId,
                "INC0000001",
                requesterId,
                categoryId,
                null,
                statusId,
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                null,
                null,
                null,
                "Short description",
                "Some description",
                "",
                false,
                null,
                dateTime,
                "",
                ACTUAL_START,
                ACTUAL_END
        );

        when(incidentService.createIncident(any(CreateIncidentRequest.class))).thenReturn(incident);

        mockMvc.perform(post("/api/v1/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "number": "INC0000001",
                                 "requesterId": "9a5241e7-862b-4004-8be6-d776c0e4bb0d",
                                 "categoryId": "886e1ce6-209c-4868-8a1c-e8061b095dc4",
                                 "subcategoryId": "",
                                 "statusId": "dda7ddae-a376-4017-a20b-e0b0f2a769d9",
                                 "priority": "LOW",
                                 "impact": "LOW",
                                 "urgency": "LOW",
                                 "ciId": null,
                                 "groupId": null,
                                 "assigneeId": null,
                                 "shortDescription": "Short description",
                                 "description": "Some description",
                                 "syncToServiceNow": "false"
                                 }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(incidentId.toString()))
                .andExpect(jsonPath("$.requesterId").value(requesterId.toString()))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.statusId").value(statusId.toString()))
                .andExpect(jsonPath("$.shortDescription").value("Short description"))
                .andExpect(jsonPath("$.description").value("Some description"));

        verify(incidentService).createIncident(any(CreateIncidentRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when create request is invalid")
    void createIncident_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "number": "",
                                "description":"Some description"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        verify(incidentService, never()).createIncident(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update incident and return 200")
    void updateStatus_shouldReturnUpdatedCategory() throws Exception {
        UUID incidentId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto incident = new IncidentDto(
                incidentId,
                "INC0000001",
                requesterId,
                categoryId,
                null,
                statusId,
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                null,
                null,
                null,
                "Updated short description",
                "Updated Description",
                "",
                false,
                null,
                dateTime,
                "",
                ACTUAL_START,
                ACTUAL_END
        );

        when(incidentService.updateIncident(any(UUID.class), any(UpdateIncidentRequest.class)))
                .thenReturn(incident);

        mockMvc.perform(put("/api/v1/incidents/{incidentId}", incidentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "requesterId": "9a5241e7-862b-4004-8be6-d776c0e4bb0d",
                          "categoryId": "172888d7-5e81-4983-846d-249780a439b4",
                          "subcategoryId": null,
                          "statusId": "dda7ddae-a376-4017-a20b-e0b0f2a769d9",
                          "priority": "LOW",
                          "impact": "LOW",
                          "urgency": "LOW",
                          "ciId": null,
                          "groupId": null,
                          "assigneeId": null,
                          "shortDescription": "Updated short description",
                          "description": "Updated Description"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(incidentId.toString()))
                .andExpect(jsonPath("$.shortDescription").value("Updated short description"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(incidentService).updateIncident(any(UUID.class), any(UpdateIncidentRequest.class));

    }

    @Test
    @WithMockUser
    @DisplayName("Should return incident by id")
    void getStatus_shouldReturnCategory() throws Exception {
        UUID incidentId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();

        IncidentDto incident = new IncidentDto(
                incidentId,
                "INC0000001",
                requesterId,
                categoryId,
                null,
                statusId,
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                null,
                null,
                null,
                "Short description",
                "Some description",
                "",
                false,
                null,
                dateTime,
                "",
                ACTUAL_START,
                ACTUAL_END
        );

        when(incidentService.getIncident(incidentId))
                .thenReturn(incident);

        mockMvc.perform(get("/api/v1/incidents/{incidentId}", incidentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(incidentId.toString()))
                .andExpect(jsonPath("$.number").value("INC0000001"))
                .andExpect(jsonPath("$.statusId").value(statusId.toString()))
                .andExpect(jsonPath("$.shortDescription").value("Short description"))
                .andExpect(jsonPath("$.description").value("Some description"));

        verify(incidentService).getIncident(incidentId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return all incident statuses")
    void getAllIncidentStatuses_shouldReturnAllIncidentCategories() throws Exception {

        UUID incidentId = UUID.randomUUID();
        UUID incidentId2 = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.now();


        IncidentDto first = new IncidentDto(
                incidentId,
                "INC0000001",
                requesterId,
                categoryId,
                null,
                statusId,
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                null,
                null,
                null,
                "Short description",
                "Some description",
                "",
                false,
                null,
                dateTime,
                "",
                ACTUAL_START,
                ACTUAL_END
        );

        IncidentDto second = new IncidentDto(
                incidentId2,
                "INC0000002",
                requesterId,
                categoryId,
                null,
                statusId,
                Priority.LOW,
                Impact.LOW,
                Urgency.LOW,
                null,
                null,
                null,
                "Short description",
                "Some description",
                "",
                false,
                null,
                dateTime,
                "",
                ACTUAL_START,
                ACTUAL_END
        );

        when(incidentService.getAllIncidents())
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].number").value("INC0000001"))
                .andExpect(jsonPath("$[1].number").value("INC0000002"));

        verify(incidentService).getAllIncidents();
    }
}
