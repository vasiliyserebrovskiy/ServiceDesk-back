package com.sitool.servicedesk.token.dto;

import java.time.Instant;

public record RefreshTokenDTO(
        String refreshToken,
        Instant createdAt,
        Instant expiredAt
) {
}
