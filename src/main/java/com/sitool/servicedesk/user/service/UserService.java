package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;

/**
 * Service for managing users.
 * Provides functionality for user registration and retrieval.
 */
public interface UserService {
    /**
     * Registers a new user.
     *
     * - Validates that email is unique
     * - Encodes user password
     * - Assigns default role
     *
     * @param registerUserRequest contains:
     * - firstname user's first name
     * - lastname user's last name
     * - email unique email address
     * - password raw password (will be encoded)
     * @return created user
     *
     * @throws UserAlreadyExistException if email is already taken
     * @throws RuntimeException if default role is not found
     */
    RegisterUserResponse createNewUser(RegisterUserRequest registerUserRequest);

}
