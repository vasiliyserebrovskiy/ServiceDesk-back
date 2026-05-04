package com.sitool.servicedesk.user.dto.response;

import java.util.UUID;

public record UserDto(
    UUID id,
    String firstname,
    String lastname,
    String email,
    String description,
    String url,
    String role
) { }
