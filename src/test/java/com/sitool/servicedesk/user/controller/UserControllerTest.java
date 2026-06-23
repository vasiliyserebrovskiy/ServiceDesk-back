package com.sitool.servicedesk.user.controller;

import com.sitool.servicedesk.security.service.AuthUserDetails;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import com.sitool.servicedesk.user.dto.request.ChangePasswordRequest;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.request.ResetPasswordRequest;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.exceptions.InvalidPasswordException;
import com.sitool.servicedesk.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should create new user and return 201")
    void createNewUser_shouldReturnCreated() throws Exception {
        UUID roleId = UUID.randomUUID();

        UserDto response = new UserDto(
                UUID.randomUUID(),
                "Vasiliy",
                "Serebrovskii",
                "vasiliy@domain.com",
                "Some description",
                "",
                roleId,
                true,
                false
        );

        when(userService.createNewUser(any(RegisterUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
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
                .andExpect(jsonPath("$.roleId").value(roleId.toString()));

        verify(userService).createNewUser(any(RegisterUserRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when request is invalid")
    void createNewUser_shouldReturnBadRequest_whenInvalidInput() throws Exception {

        mockMvc.perform(post("/api/v1/users")
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
        UUID roleId = UUID.randomUUID();
        UserDto userDto = new UserDto(
                UUID.randomUUID(),
                "Vasiliy",
                "Serebrovskii",
                "vasiliy@domain.com",
                "Some description",
                "",
                roleId,
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
                .andExpect(jsonPath("$.roleId").value(roleId.toString()));

        verify(userService).getMe(any());
    }

    @Test
    @DisplayName("Should change password successfully and return 204")
    void changePassword_shouldReturn204() throws Exception {
        UUID userId = UUID.randomUUID();
        mockAuthUser(userId);

        doNothing().when(userService).changePassword(any(UUID.class), any(ChangePasswordRequest.class));

        mockMvc.perform(patch("/api/v1/users/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "oldPassword": "OldPassword123!",
                              "newPassword": "NewPassword123!"
                            }
                            """))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 403 when user tries to change another user's password")
    void changePassword_shouldReturn403_whenChangingAnotherUsersPassword() throws Exception {
        UUID userId = UUID.randomUUID();
        mockAuthUser(UUID.randomUUID());

        mockMvc.perform(patch("/api/v1/users/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "oldPassword": "OldPassword123!",
                              "newPassword": "NewPassword123!"
                            }
                            """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 400 when old password is incorrect")
    void changePassword_shouldReturn400_whenOldPasswordIsIncorrect() throws Exception {
        UUID userId = UUID.randomUUID();
        mockAuthUser(userId);

        doThrow(new InvalidPasswordException())
                .when(userService).changePassword(any(UUID.class), any(ChangePasswordRequest.class));

        mockMvc.perform(patch("/api/v1/users/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "oldPassword": "WrongPassword123!",
                              "newPassword": "NewPassword123!"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reset password successfully and return 204")
    @WithMockUser(username = "admin@domain.com", roles = "ADMIN")
    void resetPassword_shouldReturn204() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).resetPassword(any(UUID.class), any(ResetPasswordRequest.class));

        mockMvc.perform(post("/api/v1/users/" + userId + "/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "newPassword": "NewPassword123!"
                            }
                            """))
                .andExpect(status().isNoContent());

        verify(userService).resetPassword(any(UUID.class), any(ResetPasswordRequest.class));
    }

    private void mockAuthUser(UUID userId) {
        AuthUserDetails authUserDetails = mock(AuthUserDetails.class);
        when(authUserDetails.getUserId()).thenReturn(userId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUserDetails, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}