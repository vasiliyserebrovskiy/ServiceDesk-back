package com.sitool.servicedesk.token.dto;

import java.time.Instant;

/**
 * DTO used for transferring generated refresh token data.
 *
 * @param refreshToken generated JWT refresh token
 * @param createdAt token creation timestamp (UTC)
 * @param expiredAt token expiration timestamp (UTC)
 */
public record RefreshTokenDTO(
        String refreshToken,
        Instant createdAt,
        Instant expiredAt
) {
}
