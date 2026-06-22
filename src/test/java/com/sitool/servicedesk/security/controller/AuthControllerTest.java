package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.security.service.AuthService;
import com.sitool.servicedesk.security.service.CookieService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockCookie;
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

        when(cookieService.generateAccessTokenCookie("access-token"))
                .thenReturn(ResponseCookie.from("access", "access-token").build());

        when(cookieService.generateRefreshTokenCookie("refresh-token"))
                .thenReturn(ResponseCookie.from("refresh", "refresh-token").build());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "vasiliy@domain.com",
                                  "password": "1qaZXsw@"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));
    }

    @Test
    void refreshToken_shouldReturnOk_andSetNewCookies() throws Exception {
        TokenResponseDto newTokens = new TokenResponseDto("new-access-token", "new-refresh-token");

        when(authService.refreshAccessToken(any())).thenReturn(newTokens);

        when(cookieService.generateAccessTokenCookie("new-access-token"))
                .thenReturn(ResponseCookie.from("access", "new-access-token").build());

        when(cookieService.generateRefreshTokenCookie("new-refresh-token"))
                .thenReturn(ResponseCookie.from("refresh", "new-refresh-token").build());

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .cookie(new MockCookie("refresh", "old-refresh-token")))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"));
    }

    @Test
    void logout_shouldReturnOk_andClearCookies() throws Exception {
        when(cookieService.extractRefreshToken(any())).thenReturn("refresh-token");

        when(cookieService.generateLogoutCookie(any()))
                .thenReturn(ResponseCookie.from("access", "").maxAge(0).build());

        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk());
    }
}