package com.sitool.servicedesk.token.service;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import com.sitool.servicedesk.token.entity.RefreshToken;
import com.sitool.servicedesk.token.repository.RefreshTokenRepository;
import com.sitool.servicedesk.token.utils.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Persists refresh token in database in hashed form.
     *
     * We do NOT store raw JWT refresh token for security reasons.
     * Instead, we store only its hash.
     */
    @Transactional
    public void saveRefreshToken(UUID userId, String token, Instant createdAt, Instant expiresAt) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);

        // Security: store only hashed token, not raw JWT
        refreshToken.setTokenHash(TokenHasher.generateRefreshTokenHash(token));
        refreshToken.setCreatedAt(createdAt);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * Validates refresh token:
     * - checks existence in DB
     * - checks revocation status
     * - checks expiration
     *
     * @throws RestApiException if token is invalid, expired or revoked
     */
    public RefreshToken validateRefreshToken(String token) {
        String hashToken = TokenHasher.generateRefreshTokenHash(token);

        RefreshToken stored = refreshTokenRepository
                .findByTokenHash(hashToken)
                .orElseThrow(() -> new RestApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh Token"));

        if (stored.isRevoked()) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Token revoked");
        }

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new RestApiException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        return stored;
    }

    /**
     * Marks refresh token as revoked in memory.
     * Caller is responsible for persisting the change if needed.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeRefreshToken(RefreshToken stored) {
        refreshTokenRepository.revokeByToken(stored.getTokenHash());
    }

    /**
     * Logs out user by revoking refresh token in DB.
     */
    @Transactional
    public void logout(String refreshToken) {

        String hash = TokenHasher.generateRefreshTokenHash(refreshToken);

        refreshTokenRepository.findByTokenHash(hash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }
}
