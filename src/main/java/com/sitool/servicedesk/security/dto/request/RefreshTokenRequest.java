package com.sitool.servicedesk.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request that contains refresh token for getting new access token purpose
 */
@Schema(description = "Data Transfer Object for delivering refresh tokens")
public record RefreshTokenRequest(

        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9...0ADS106w")
        @JsonProperty("refreshToken")
        String refreshToken

) {
}
