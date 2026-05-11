package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.token.dto.RefreshTokenDTO;
import com.sitool.servicedesk.token.entity.RefreshToken;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User user;
    private AuthUserDetails userDetails;

    @BeforeEach
    void setUp() {

        user = new User();
        UUID userId = UUID.randomUUID();

        ReflectionTestUtils.setField(user, "id", userId);
        user.setEmail("test@test.com");

        userDetails = new AuthUserDetails(user);
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() {

        LoginUserRequest request =
                new LoginUserRequest("test@test.com", "password");

        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO(
                "refresh-token",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtTokenService.generateAccessToken(any()))
                .thenReturn("access-token");

        when(jwtTokenService.generateRefreshToken(any()))
                .thenReturn(refreshTokenDTO);

        TokenResponseDto result = authService.login(request);

        assertNotNull(result);
        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());

        verify(refreshTokenService).saveRefreshToken(
                eq(user.getId()),
                eq("refresh-token"),
                any(),
                any()
        );
    }

    @Test
    void login_shouldThrowException_whenUserDisabled() {

        LoginUserRequest request =
                new LoginUserRequest("test@test.com", "password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new DisabledException("Disabled"));

        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> authService.login(request)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_whenUserLocked() {

        LoginUserRequest request =
                new LoginUserRequest("test@test.com", "password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new LockedException("Locked"));

        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> authService.login(request)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_whenCredentialsInvalid() {

        LoginUserRequest request =
                new LoginUserRequest("test@test.com", "password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> authService.login(request)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void refreshAccessToken_shouldReturnNewTokens() {

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId(user.getId());

        RefreshTokenDTO newRefreshToken = new RefreshTokenDTO(
                "new-refresh-token",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        when(refreshTokenService.validateRefreshToken("old-refresh-token"))
                .thenReturn(storedToken);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(jwtTokenService.generateAccessToken(user.getEmail()))
                .thenReturn("new-access-token");

        when(jwtTokenService.generateRefreshToken(user.getEmail()))
                .thenReturn(newRefreshToken);

        TokenResponseDto result =
                authService.refreshAccessToken("old-refresh-token");

        assertNotNull(result);

        assertEquals("new-access-token", result.accessToken());
        assertEquals("new-refresh-token", result.refreshToken());

        verify(refreshTokenService)
                .revokeRefreshToken(storedToken);

        verify(refreshTokenService)
                .saveRefreshToken(
                        eq(user.getId()),
                        eq("new-refresh-token"),
                        any(),
                        any()
                );
    }

    @Test
    void refreshAccessToken_shouldThrowException_whenUserNotFound() {

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId(UUID.randomUUID());

        when(refreshTokenService.validateRefreshToken(any()))
                .thenReturn(storedToken);

        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> authService.refreshAccessToken("refresh-token")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }
}