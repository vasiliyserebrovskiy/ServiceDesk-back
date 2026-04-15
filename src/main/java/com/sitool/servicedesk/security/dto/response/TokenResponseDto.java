package com.sitool.servicedesk.security.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Response that contains access and refresh tokens generated for User during login process
 */
@Schema(name = "TokenResponse", description = "Response payload containing access and refresh tokens")
public record TokenResponseDto(
        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...0ADS106w")
        String accessToken,

        @Schema(description = "JWT refresh token", example = "eyJhbGciOiJIUzI1NiJ9...joEW3Hv8Yrs")
        String refreshToken
) {
}
