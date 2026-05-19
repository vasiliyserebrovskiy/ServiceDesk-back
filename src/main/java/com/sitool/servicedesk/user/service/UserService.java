package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing user accounts.
 *
 * <p>Provides operations for user registration, retrieval of authenticated user data,
 * and administration-related user management functions.</p>
 */
public interface UserService {
    /**
     * Creates a new user account.
     *
     * @param registerUserRequest request containing user registration data
     * @return response with created user information
     *
     * @throws UserAlreadyExistException if a user with the given email already exists
     */
    RegisterUserResponse createNewUser(RegisterUserRequest registerUserRequest);

    /**
     * Returns information about the currently authenticated user.
     *
     * @param email email of the authenticated user
     * @return user data transfer object
     */
    UserDto getMe(String email);

    /**
     * Creates a default administrator account during application startup
     * if it does not already exist in the system.
     */
    void createAdminIfNotExists();

    /**
     * Retrieves a list of all registered users.
     *
     * @return list of user DTOs
     */
    List<UserDto> getAllUsers();

    /**
     * Retrieves user information by unique user identifier.
     *
     * @param userId unique identifier of the user (UUID)
     * @return user data transfer object
     */
    UserDto getUser(UUID userId);
}
