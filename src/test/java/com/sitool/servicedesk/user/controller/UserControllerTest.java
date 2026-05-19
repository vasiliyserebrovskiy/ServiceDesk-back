package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        UserService userService() {
            return mock(UserService.class);
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
    @DisplayName("Should create new user and return 201")
    void createNewUser_shouldReturnCreated() throws Exception {

        RegisterUserResponse response = new RegisterUserResponse(
                UUID.randomUUID(),
                "Vasiliy",
                "Serebrovskii",
                "vasiliy@domain.com",
                "USER",
                "",
                ""
        );

        when(userService.createNewUser(any(RegisterUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstname": "Vasiliy",
                                  "lastname": "Serebrovskii",
                                  "email": "vasiliy@domain.com",
                                  "password": "StrongPassword123!",
                                  "role": "USER",
                                  "description": "Test user",
                                  "avatarUrl": ""
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("Vasiliy"))
                .andExpect(jsonPath("$.lastname").value("Serebrovskii"))
                .andExpect(jsonPath("$.email").value("vasiliy@domain.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).createNewUser(any(RegisterUserRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when request is invalid")
    void createNewUser_shouldReturnBadRequest_whenInvalidInput() throws Exception {

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstname": "",
                                  "lastname": "",
                                  "email": "wrong-email",
                                  "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createNewUser(any());
    }

    @Test
    @WithMockUser(
            username = "vasiliy@domain.com",
            roles = "USER"
    )
    @DisplayName("Should return current authenticated user")
    void getUser_shouldReturnCurrentUser() throws Exception {

        UserDto userDto = new UserDto(
                UUID.randomUUID(),
                "Vasiliy",
                "Serebrovskii",
                "vasiliy@domain.com",
                "",
                "",
                "USER",
                true,
                false
        );

        when(userService.getMe(any()))
                .thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Vasiliy"))
                .andExpect(jsonPath("$.lastname").value("Serebrovskii"))
                .andExpect(jsonPath("$.email").value("vasiliy@domain.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).getMe(any());
    }
}