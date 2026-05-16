package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;

/**
 * Service for managing user accounts.
 *
 * <p>Provides operations for user registration and retrieval
 * of authenticated user data.</p>
 */
public interface UserService {
    /**
     * Creates a new user account.
     *
     * @param registerUserRequest registration data
     * @return created user representation
     *
     * @throws UserAlreadyExistException if email is already registered
     */
    RegisterUserResponse createNewUser(RegisterUserRequest registerUserRequest);

    /**
     * Returns user information by email.
     *
     * @param email user email
     * @return user data transfer object
     */
    UserDto getUser(String email);

    /**
     * Creates the default administrator account
     * during application startup if it does not already exist.
     */
    void createAdminIfNotExists();

}
