package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import org.springframework.security.core.Authentication;

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

    UserDto getUser(Authentication authentication);

}
