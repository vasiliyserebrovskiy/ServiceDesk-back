package com.sitool.servicedesk.group.controller;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.service.GroupService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GroupControllerTest.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupService groupService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        GroupService groupService() {
            return mock(GroupService.class);
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
    @DisplayName("Should create group and return 201")
    void createGroup_shouldReturnCreated() throws Exception {

        UUID userId = UUID.randomUUID();

        GroupDto response = new GroupDto(
                UUID.randomUUID(),
                "Support Team",
                "Some description",
                List.of(userId)
        );

        when(groupService.createGroup(any(CreateGroupRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Support Team",
                                  "description": "Some description",
                                  "userIds": [
                                    "%s"
                                  ]
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Support Team"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.userIds[0]").value(userId.toString()));

        verify(groupService).createGroup(any(CreateGroupRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when create request is invalid")
    void createGroup_shouldReturnBadRequest_whenInvalidInput() throws Exception {

        mockMvc.perform(post("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(groupService, never()).createGroup(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update group and return 200")
    void updateGroup_shouldReturnUpdatedGroup() throws Exception {

        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        GroupDto response = new GroupDto(
                groupId,
                "Updated Team",
                "Updated description",
                List.of(userId)
        );

        when(groupService.updateGroup(any(UUID.class), any(UpdateGroupRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Team",
                                  "description": "Updated description"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(groupId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Team"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(groupService).updateGroup(any(UUID.class), any(UpdateGroupRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should delete group and return 204")
    void deleteGroup_shouldReturnNoContent() throws Exception {

        UUID groupId = UUID.randomUUID();

        doNothing().when(groupService).deleteGroup(groupId);

        mockMvc.perform(delete("/api/v1/groups/{groupId}", groupId))
                .andExpect(status().isNoContent());

        verify(groupService).deleteGroup(groupId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return group by id")
    void getGroup_shouldReturnGroup() throws Exception {

        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        GroupDto response = new GroupDto(
                groupId,
                "Support Team",
                "Some description",
                List.of(userId)
        );

        when(groupService.getGroup(groupId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/groups/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(groupId.toString()))
                .andExpect(jsonPath("$.name").value("Support Team"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.userIds[0]").value(userId.toString()));

        verify(groupService).getGroup(groupId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return all groups")
    void getAllGroups_shouldReturnListOfGroups() throws Exception {

        GroupDto first = new GroupDto(
                UUID.randomUUID(),
                "Support Team",
                "First description",
                List.of()
        );

        GroupDto second = new GroupDto(
                UUID.randomUUID(),
                "Dev Team",
                "Second description",
                List.of()
        );

        when(groupService.getAllGroups())
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Support Team"))
                .andExpect(jsonPath("$[1].name").value("Dev Team"));

        verify(groupService).getAllGroups();
    }
}