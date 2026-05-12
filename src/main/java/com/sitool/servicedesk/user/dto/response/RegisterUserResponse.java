package com.sitool.servicedesk.user.dto.response;

import java.util.UUID;

/**
 * Represents the response returned after a successful user registration.
 *
 * <p>This DTO exposes only publicly safe user attributes that are returned
 * to the client after account creation.</p>
 */
public record RegisterUserResponse(
    UUID id,
    String firstname,
    String lastname,
    String email,
    String role
) {
}
