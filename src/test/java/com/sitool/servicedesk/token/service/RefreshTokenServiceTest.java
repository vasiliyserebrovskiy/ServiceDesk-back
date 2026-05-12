package com.sitool.servicedesk.token.service;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import com.sitool.servicedesk.token.entity.RefreshToken;
import com.sitool.servicedesk.token.repository.RefreshTokenRepository;
import com.sitool.servicedesk.token.utils.TokenHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private final String rawToken = "raw-refresh-token";
    private final String hashedToken = TokenHasher.generateRefreshTokenHash(rawToken);

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // nothing needed here for now
    }

    @Test
    void saveRefreshToken_shouldSaveHashedToken() {

        // when
        refreshTokenService.saveRefreshToken(
                userId,
                rawToken,
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        // then
        ArgumentCaptor<RefreshToken> captor =
                ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository, times(1))
                .save(captor.capture());

        RefreshToken saved = captor.getValue();

        assertEquals(userId, saved.getUserId());
        assertEquals(hashedToken, saved.getTokenHash());
        assertFalse(saved.isRevoked());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getExpiresAt());
    }

    @Test
    void validateRefreshToken_shouldReturnToken_whenValid() {

        RefreshToken stored = new RefreshToken();
        stored.setUserId(userId);
        stored.setTokenHash(hashedToken);
        stored.setCreatedAt(Instant.now());
        stored.setExpiresAt(Instant.now().plusSeconds(3600));
        stored.setRevoked(false);

        when(refreshTokenRepository.findByTokenHash(hashedToken))
                .thenReturn(Optional.of(stored));

        RefreshToken result =
                refreshTokenService.validateRefreshToken(rawToken);

        assertEquals(stored, result);
    }

    @Test
    void validateRefreshToken_shouldThrow_whenTokenNotFound() {

        when(refreshTokenRepository.findByTokenHash(hashedToken))
                .thenReturn(Optional.empty());

        assertThrows(RestApiException.class,
                () -> refreshTokenService.validateRefreshToken(rawToken));
    }

    @Test
    void validateRefreshToken_shouldThrow_whenRevoked() {

        RefreshToken stored = new RefreshToken();
        stored.setTokenHash(hashedToken);
        stored.setRevoked(true);
        stored.setExpiresAt(Instant.now().plusSeconds(3600));

        when(refreshTokenRepository.findByTokenHash(hashedToken))
                .thenReturn(Optional.of(stored));

        assertThrows(RestApiException.class,
                () -> refreshTokenService.validateRefreshToken(rawToken));
    }

    @Test
    void validateRefreshToken_shouldThrow_whenExpired() {

        RefreshToken stored = new RefreshToken();
        stored.setTokenHash(hashedToken);
        stored.setRevoked(false);
        stored.setExpiresAt(Instant.now().minusSeconds(10));

        when(refreshTokenRepository.findByTokenHash(hashedToken))
                .thenReturn(Optional.of(stored));

        assertThrows(RestApiException.class,
                () -> refreshTokenService.validateRefreshToken(rawToken));
    }

    @Test
    void revokeRefreshToken_shouldSetRevokedTrue() {

        RefreshToken token = new RefreshToken();
        token.setRevoked(false);

        refreshTokenService.revokeRefreshToken(token);

        assertTrue(token.isRevoked());
    }

    @Test
    void logout_shouldRevokeAndSaveToken_whenExists() {

        RefreshToken stored = new RefreshToken();
        stored.setTokenHash(hashedToken);
        stored.setRevoked(false);

        when(refreshTokenRepository.findByTokenHash(hashedToken))
                .thenReturn(Optional.of(stored));

        refreshTokenService.logout(rawToken);

        assertTrue(stored.isRevoked());
        verify(refreshTokenRepository, times(1)).save(stored);
    }

    @Test
    void logout_shouldDoNothing_whenTokenNotFound() {

        when(refreshTokenRepository.findByTokenHash(hashedToken))
                .thenReturn(Optional.empty());

        refreshTokenService.logout(rawToken);

        verify(refreshTokenRepository, never()).save(any());
    }
}