package com.sitool.servicedesk.user.dto.response;

public record RegisterUserResponse(
    Long id,
    String firstname,
    String lastname,
    String email,
    String role
) {
}
