package com.sitool.servicedesk.token.repository;

import com.sitool.servicedesk.token.entity.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("Should find refresh token by tokenHash")
    void shouldFindByTokenHash() {

        // given
        RefreshToken token = new RefreshToken();
        token.setUserId(UUID.randomUUID());
        token.setTokenHash("hash-123");
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        token.setRevoked(false);
        token.setDeviceInfo("chrome");

        refreshTokenRepository.save(token);

        // when
        Optional<RefreshToken> result =
                refreshTokenRepository.findByTokenHash("hash-123");

        // then
        assertTrue(result.isPresent());
        assertEquals("hash-123", result.get().getTokenHash());
        assertEquals("chrome", result.get().getDeviceInfo());
        assertFalse(result.get().isRevoked());
    }

    @Test
    @DisplayName("Should return empty when tokenHash not found")
    void shouldReturnEmptyWhenTokenHashNotFound() {

        // given
        RefreshToken token = new RefreshToken();
        token.setUserId(UUID.randomUUID());
        token.setTokenHash("existing-hash");
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        token.setRevoked(false);

        refreshTokenRepository.save(token);

        // when
        Optional<RefreshToken> result =
                refreshTokenRepository.findByTokenHash("wrong-hash");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should persist revoked flag correctly")
    void shouldPersistRevokedFlag() {

        // given
        RefreshToken token = new RefreshToken();
        token.setUserId(UUID.randomUUID());
        token.setTokenHash("hash-revoked");
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        token.setRevoked(true);

        refreshTokenRepository.save(token);

        // when
        RefreshToken found = refreshTokenRepository
                .findByTokenHash("hash-revoked")
                .orElseThrow();

        // then
        assertTrue(found.isRevoked());
    }
}