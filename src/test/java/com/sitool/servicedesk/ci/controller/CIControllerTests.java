package com.sitool.servicedesk.ci.controller;

import com.sitool.servicedesk.ci.dto.request.CreateCIRequest;
import com.sitool.servicedesk.ci.dto.request.UpdateCIRequest;
import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.ci.service.CIService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CIController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CIControllerTests.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
public class CIControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CIService ciService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        CIService ciService() {return mock(CIService.class);}

        @Bean
        JwtTokenService  jwtTokenService() {return mock(JwtTokenService.class);}

        @Bean
        CustomUserDetailsService customUserDetailsService() {return mock(CustomUserDetailsService.class);}

        @Bean
        RefreshTokenService  refreshTokenService() {return mock(RefreshTokenService.class);}
    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(ciService);
    }

    @Test
    @WithMockUser
    @DisplayName("Should create configuration item and return 201")
    void createCI_shouldReturnCreated() throws Exception {

        CIDto response = new CIDto(
                UUID.randomUUID(),
                "Core-SW-01",
                "Core network switch located in server room A",
                "Network Equipment",
                "Cisco",
                "FCW2142L0QK",
                "Catalyst 9300"
        );

        when(ciService.createCI(any(CreateCIRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/cis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Core-SW-01",
                                "description":"Core network switch located in server room A",
                                "type": "Network Equipment",
                                "manufacturer": "Cisco",
                                "serialNumber": "FCW2142L0QK",
                                "model": "Catalyst 9300"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Core-SW-01"))
                .andExpect(jsonPath("$.description").value("Core network switch located in server room A"))
                .andExpect(jsonPath("$.type").value("Network Equipment"))
                .andExpect(jsonPath("$.manufacturer").value("Cisco"))
                .andExpect(jsonPath("$.serialNumber").value("FCW2142L0QK"))
                .andExpect(jsonPath("$.model").value("Catalyst 9300"));

        verify(ciService).createCI(any(CreateCIRequest.class));

    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when create request is invalid")
    void createCI_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/cis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "description":"Some description"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        verify(ciService, never()).createCI(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update configuration item and return 200")
    void updateCI_shouldReturnUpdatedCategory() throws Exception {
        UUID ciId = UUID.randomUUID();

        CIDto response = new CIDto(
                ciId,
                "Core-SW-02",
                "Core network switch located in server room A",
                "Network Equipment",
                "Cisco",
                "FCW2142L0QK",
                "Catalyst 9300"
        );

        when(ciService.updateCI(any(UUID.class), any(UpdateCIRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/cis/{ciId}", ciId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Core-SW-02",
                          "description":"Core network switch located in server room A",
                          "type": "Network Equipment",
                          "manufacturer": "Cisco",
                          "serialNumber": "FCW2142L0QK",
                          "model": "Catalyst 9300"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ciId.toString()))
                .andExpect(jsonPath("$.name").value("Core-SW-02"))
                .andExpect(jsonPath("$.description").value("Core network switch located in server room A"));

        verify(ciService).updateCI(any(UUID.class), any(UpdateCIRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should delete configuration item and return 204")
    void deleteCI_shouldReturnNoContent() throws Exception {

        UUID ciId = UUID.randomUUID();

        doNothing().when(ciService).deleteCI(ciId);

        mockMvc.perform(delete("/api/v1/cis/{ciId}", ciId))
                .andExpect(status().isNoContent());

        verify(ciService).deleteCI(ciId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return configuration item by id")
    void getCategory_shouldReturnCategory() throws Exception {
        UUID ciId = UUID.randomUUID();

        CIDto response = new CIDto(
                ciId,
                "Core-SW-01",
                "Core network switch located in server room A",
                "Network Equipment",
                "Cisco",
                "FCW2142L0QK",
                "Catalyst 9300"
        );

        when(ciService.getCIById(ciId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/cis/{cis}", ciId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ciId.toString()))
                .andExpect(jsonPath("$.name").value("Core-SW-01"))
                .andExpect(jsonPath("$.description").value("Core network switch located in server room A"))
                .andExpect(jsonPath("$.type").value("Network Equipment"))
                .andExpect(jsonPath("$.manufacturer").value("Cisco"))
                .andExpect(jsonPath("$.serialNumber").value("FCW2142L0QK"))
                .andExpect(jsonPath("$.model").value("Catalyst 9300"));

        verify(ciService).getCIById(ciId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return all incident categories")
    void getAllIncidentCategories_shouldReturnAllIncidentCategories() throws Exception {
        CIDto first = new CIDto(
                UUID.randomUUID(),
                "Core-SW-01",
                "Core network switch located in server room A",
                "Network Equipment",
                "Cisco",
                "FCW2142L0QK",
                "Catalyst 9300"
        );

        CIDto second = new CIDto(
                UUID.randomUUID(),
                "Core-SW-02",
                "Core network switch located in server room A",
                "Network Equipment",
                "Cisco",
                "FCW2142L0QK",
                "Catalyst 9300"
        );

        when(ciService.getAllCI())
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/cis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Core-SW-01"))
                .andExpect(jsonPath("$[1].name").value("Core-SW-02"));

        verify(ciService).getAllCI();
    }
}
