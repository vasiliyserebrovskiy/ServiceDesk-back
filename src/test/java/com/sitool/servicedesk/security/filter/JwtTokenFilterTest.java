package com.sitool.servicedesk.security.filter;

import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static com.sitool.servicedesk.security.constants.Constants.ACCESS_TOKEN_COOKIE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private FilterChain filterChain;

    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        jwtTokenFilter = new JwtTokenFilter(userDetailsService, jwtTokenService);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldAuthenticateUser_whenTokenIsValid()
            throws ServletException, IOException {

        String token = "valid-token";
        String email = "user@test.com";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new MockCookie(ACCESS_TOKEN_COOKIE, token));

        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDetails userDetails = User.withUsername(email)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(jwtTokenService.validateToken(
                token,
                JwtTokenService.TokenType.ACCESS)
        ).thenReturn(true);

        when(jwtTokenService.getUsernameFromToken(
                token,
                JwtTokenService.TokenType.ACCESS)
        ).thenReturn(email);

        when(userDetailsService.loadUserByUsername(email))
                .thenReturn(userDetails);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        assertEquals(
                email,
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldReturn401_whenTokenExpired()
            throws ServletException, IOException {

        String token = "expired-token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new MockCookie(ACCESS_TOKEN_COOKIE, token));

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtTokenService.validateToken(
                token,
                JwtTokenService.TokenType.ACCESS)
        ).thenThrow(mock(ExpiredJwtException.class));

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());

        verify(filterChain, never()).doFilter(any(), any());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldContinueChain_whenTokenMissing()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();

        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldUseAuthorizationHeader_whenCookieMissing()
            throws ServletException, IOException {

        String token = "header-token";
        String email = "user@test.com";

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDetails userDetails = User.withUsername(email)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(jwtTokenService.validateToken(
                token,
                JwtTokenService.TokenType.ACCESS)
        ).thenReturn(true);

        when(jwtTokenService.getUsernameFromToken(
                token,
                JwtTokenService.TokenType.ACCESS)
        ).thenReturn(email);

        when(userDetailsService.loadUserByUsername(email))
                .thenReturn(userDetails);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenTokenIsInvalid()
            throws ServletException, IOException {

        String token = "invalid-token";
        String email = "user@test.com";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new MockCookie(ACCESS_TOKEN_COOKIE, token));

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtTokenService.validateToken(
                token,
                JwtTokenService.TokenType.ACCESS)
        ).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Authentication must NOT be installed
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // The filter chain should continue
        verify(filterChain).doFilter(request, response);

        // there should be no interactions with the UserDetailsService.
        verifyNoInteractions(userDetailsService);
    }
}
