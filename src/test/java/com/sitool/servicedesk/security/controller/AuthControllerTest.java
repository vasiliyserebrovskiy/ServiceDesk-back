package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.security.service.AuthService;
import com.sitool.servicedesk.security.service.CookieService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthControllerTest.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private CookieService cookieService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        AuthService authService() {
            return mock(AuthService.class);
        }

        @Bean
        CookieService cookieService() {
            return mock(CookieService.class);
        }

        @Bean
        RefreshTokenService refreshTokenService() {
            return mock(RefreshTokenService.class);
        }

        @Bean
        CustomUserDetailsService customUserDetailsService() {
            return mock(CustomUserDetailsService.class);
        }

        @Bean
        JwtTokenService jwtTokenService() {
            return mock(JwtTokenService.class);
        }
    }

    @Test
    void login_shouldReturnOk_andSetCookies() throws Exception {

        TokenResponseDto tokens = new TokenResponseDto("access-token", "refresh-token");

        when(authService.login(any())).thenReturn(tokens);

        Cookie accessCookie = new Cookie("access", "access-token");
        Cookie refreshCookie = new Cookie("refresh", "refresh-token");

        when(cookieService.generateAccessTokenCookie("access-token"))
                .thenReturn(accessCookie);

        when(cookieService.generateRefreshTokenCookie("refresh-token"))
                .thenReturn(refreshCookie);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "vasiliy@domain.com",
                                  "password": "1qaZXsw@"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("access"))
                .andExpect(cookie().exists("refresh"));
    }

    @Test
    void refreshToken_shouldReturnOk_andSetNewCookies() throws Exception {

        String oldRefreshToken = "old-refresh-token";

        TokenResponseDto newTokens =
                new TokenResponseDto("new-access-token", "new-refresh-token");

        when(authService.refreshAccessToken(any())).thenReturn(newTokens);

        Cookie newAccessCookie = new Cookie("access", "new-access-token");
        Cookie newRefreshCookie = new Cookie("refresh", "new-refresh-token");

        when(cookieService.generateAccessTokenCookie("new-access-token"))
                .thenReturn(newAccessCookie);

        when(cookieService.generateRefreshTokenCookie("new-refresh-token"))
                .thenReturn(newRefreshCookie);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .cookie(new Cookie("refresh", oldRefreshToken)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("access"))
                .andExpect(cookie().exists("refresh"));
    }

    @Test
    void logout_shouldReturnOk_andClearCookies() throws Exception {

        when(cookieService.extractRefreshToken(any()))
                .thenReturn("refresh-token");

        Cookie logoutCookie = new Cookie("access", "");

        when(cookieService.generateLogoutCookie(any()))
                .thenReturn(logoutCookie);

        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk());
    }
}