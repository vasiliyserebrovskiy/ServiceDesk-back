package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.token.dto.RefreshTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    private Clock fixedClock;

    private final String accessSecret =
            Base64.getEncoder()
                    .encodeToString("access-secret-key-1234567890123456".getBytes());

    private final String refreshSecret =
            Base64.getEncoder()
                    .encodeToString("refresh-secret-key-1234567890123456".getBytes());

    @BeforeEach
    void setUp() {

        fixedClock = Clock.fixed(
                Instant.parse("2026-01-01T00:00:00Z"),
                ZoneOffset.UTC
        );

        jwtTokenService = new JwtTokenService(
                accessSecret,
                refreshSecret,
                fixedClock
        );
    }

    @Test
    void generateAndParseAccessToken_shouldReturnCorrectUsername() {

        String token = jwtTokenService.generateAccessToken("test@test.com");

        String username = jwtTokenService.getUsernameFromToken(
                token,
                JwtTokenService.TokenType.ACCESS
        );

        assertEquals("test@test.com", username);
    }

    @Test
    void validateToken_shouldReturnTrue_forValidAccessToken() {

        String token = jwtTokenService.generateAccessToken("test@test.com");

        boolean valid = jwtTokenService.validateToken(
                token,
                JwtTokenService.TokenType.ACCESS
        );

        assertTrue(valid);
    }

    @Test
    void validateToken_shouldReturnFalse_forInvalidToken() {

        String fakeToken = "invalid.token.value";

        boolean valid = jwtTokenService.validateToken(
                fakeToken,
                JwtTokenService.TokenType.ACCESS
        );

        assertFalse(valid);
    }

    @Test
    void generateRefreshToken_shouldReturnValidStructure() {

        RefreshTokenDTO dto =
                jwtTokenService.generateRefreshToken("test@test.com");

        assertNotNull(dto.refreshToken());
        assertNotNull(dto.createdAt());
        assertNotNull(dto.expiredAt());

        String username = jwtTokenService.getUsernameFromToken(
                dto.refreshToken(),
                JwtTokenService.TokenType.REFRESH
        );

        assertEquals("test@test.com", username);
    }

    @Test
    void accessAndRefreshTokens_shouldBeDifferent() {

        String access =
                jwtTokenService.generateAccessToken("test@test.com");

        String refresh =
                jwtTokenService.generateRefreshToken("test@test.com")
                        .refreshToken();

        assertNotEquals(access, refresh);
    }
}