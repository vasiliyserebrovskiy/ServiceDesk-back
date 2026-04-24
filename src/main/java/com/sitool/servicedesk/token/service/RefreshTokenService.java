package com.sitool.servicedesk.token.service;

import com.sitool.servicedesk.exceptions.common.RestApiException;
import com.sitool.servicedesk.token.entity.RefreshToken;
import com.sitool.servicedesk.token.repository.RefreshTokenRepository;
import com.sitool.servicedesk.token.utils.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;




@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.rt.live-in-min}")
    private int refreshTokenLiveInMinutes;

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(UUID userId, String token, Instant createdAt, Instant expiresAt) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setTokenHash(TokenHasher.generateRefreshTokenHash(token)); //need to made a hash of a token
        refreshToken.setCreatedAt(createdAt);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
    }

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

    public void revokeRefreshToken(RefreshToken stored) {
        stored.setRevoked(true);
        //refreshTokenRepository.save(stored);
    }

    public void logout(String refreshToken) {

        String hash = TokenHasher.generateRefreshTokenHash(refreshToken);

        refreshTokenRepository.findByTokenHash(hash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }
}
