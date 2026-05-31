package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.user.dto.request.ChangePasswordRequest;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.request.ResetPasswordRequest;
import com.sitool.servicedesk.user.dto.request.UpdateUserDto;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import com.sitool.servicedesk.user.exceptions.InvalidPasswordException;
import com.sitool.servicedesk.user.exceptions.UserNotFoundException;

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
    UserDto createNewUser(RegisterUserRequest registerUserRequest);

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

    /**
     * Updates an existing user.
     *
     * @param userId identifier of the user to update
     * @param updateUserDto DTO containing updated user information
     * @return updated user data
     */
    UserDto updateUser(UUID userId, UpdateUserDto updateUserDto);

    /**
     * Changes the password for the specified user.
     *
     * <p>Requires the current password to be provided for verification
     * before applying the change.</p>
     *
     * @param userId  the UUID of the user whose password is to be changed
     * @param request the request containing the current and new passwords
     * @throws UserNotFoundException    if no user with the given ID exists
     * @throws InvalidPasswordException if the provided current password is incorrect
     */
    void changePassword(UUID userId, ChangePasswordRequest request);

    /**
     * Resets the password for the specified user.
     *
     * <p>Administrative operation that sets a new password without requiring
     * the current password.</p>
     *
     * @param userId  the UUID of the user whose password is to be reset
     * @param request the request containing the new password
     * @throws UserNotFoundException if no user with the given ID exists
     */
    void resetPassword(UUID userId, ResetPasswordRequest request);
}
