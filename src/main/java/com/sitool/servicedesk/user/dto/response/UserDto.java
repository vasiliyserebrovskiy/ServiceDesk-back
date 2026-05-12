package com.sitool.servicedesk.user.dto.response;

import java.util.UUID;

/**
 * Response DTO for the currently authenticated user.
 *
 * <p>Returned by user profile endpoint to provide client-side access
 * to basic account and profile information.</p>
 *
 * <p>Does not contain sensitive or security-related data.</p>
 */
public record UserDto(
    UUID id,
    String firstname,
    String lastname,
    String email,
    String description,
    String url,
    String role
) { }
