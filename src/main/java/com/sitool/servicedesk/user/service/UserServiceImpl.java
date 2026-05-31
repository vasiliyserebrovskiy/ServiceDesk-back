package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.exceptions.RoleNotExistException;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.user.dto.request.ChangePasswordRequest;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.request.ResetPasswordRequest;
import com.sitool.servicedesk.user.dto.request.UpdateUserDto;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.exceptions.InvalidPasswordException;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import com.sitool.servicedesk.user.exceptions.UserNotFoundException;
import com.sitool.servicedesk.user.repository.UserRepository;
import com.sitool.servicedesk.userprofile.entity.UserProfile;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.sitool.servicedesk.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Method for creating new user
     */

    @Override
    @Transactional
    public UserDto createNewUser(RegisterUserRequest registerUserRequest) {

        final String normalizedEmail = registerUserRequest.email().toLowerCase().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            log.info("User with email {} already exists", normalizedEmail);
            throw new UserAlreadyExistException();
        }

        Role role = roleRepository.findByName(registerUserRequest.role())
                .orElseThrow(RoleNotExistException::new);

        String encodedPassword = passwordEncoder.encode(registerUserRequest.password());

        // 1. create user
        User newUser = new User(normalizedEmail, encodedPassword);
        newUser.setRole(role);
        newUser.setActive(true);
        newUser.setBlocked(false);

        // 2. create profile
        UserProfile profile = new UserProfile(registerUserRequest.firstname(), registerUserRequest.lastname());

        if (registerUserRequest.description() != null && !registerUserRequest.description().isBlank()) {
            profile.setDescription(registerUserRequest.description());
        }

        // We did not implement avatar for now!
        if (registerUserRequest.avatarUrl() != null && !registerUserRequest.avatarUrl().isBlank()) {
            profile.setAvatarUrl(registerUserRequest.avatarUrl());
        }
        profile.setAvatarUrl("");

        //3. set profile user
        profile.setUser(newUser);

        // 4. set user profile
        newUser.setProfile(profile);

        userRepository.save(newUser);

        log.info("New User {} has been created.", newUser.getId());

        return userMapper.toDto(newUser);
    }

    /**
     * Method for getting user information from database
     */
    @Override
    @Transactional(readOnly=true)
    public UserDto getMe(String email) {

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(currentUser);

    }

    /**
     * Creates the default administrator user during application startup.
     */
    @Override
    public void createAdminIfNotExists() {
        if (userRepository.existsByEmail("admin@domain.com")) {
            return;
        }
        User user = new User();
        UserProfile profile = new UserProfile();

        user.setEmail("admin@domain.com");
        user.setPassword(passwordEncoder.encode("P@ssw0rd"));
        user.setActive(true);

        Role adminRole = roleRepository
                .findByName("ADMIN")
                .orElseThrow(() -> {

                    log.error("Role ADMIN was not found in database!");

                    return new IllegalStateException(
                            "Role ADMIN not found."
                    );
                });

        user.setRole(adminRole);
        profile.setFirstname("Admin");
        profile.setLastname("Administrator");
        profile.setDescription("Default administrator account. " +
                "Deactivate this account after creating a personal admin user.");
        profile.setAvatarUrl("");

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
    }

    /**
     * Retrieves all users from the system.
     */
    @Override
    @Transactional(readOnly=true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId unique user UUID
     */
    @Override
    @Transactional(readOnly=true)
    public UserDto getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(user);
    }

    /**
     * Applies partial updates to an existing user and returns the updated representation.
     *
     * @param userId identifier of the user to be updated
     * @param updateUserDto DTO containing fields to update
     * @return updated user DTO after persistence
     */
    @Override
    @Transactional
    public UserDto updateUser(UUID userId, UpdateUserDto updateUserDto) {

        boolean userIsChanged = false;

        // find user first
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found in database! UserId: {}", userId);
                    return new UserNotFoundException();
                });

        UserProfile updatedProfile = updatedUser.getProfile();

        if (!updatedProfile.getFirstname().equals(updateUserDto.firstname())) {
            userIsChanged = true;
            updatedProfile.setFirstname(updateUserDto.firstname());
        }

        if (!updatedProfile.getLastname().equals(updateUserDto.lastname())) {
            userIsChanged = true;
            updatedProfile.setLastname(updateUserDto.lastname());
        }

        if (!Objects.equals(updatedProfile.getDescription(),updateUserDto.description())) {
            userIsChanged = true;
            updatedProfile.setDescription(updateUserDto.description());
        }

        // not implemented for now
        if (!Objects.equals(updatedProfile.getAvatarUrl(), updateUserDto.avatarUrl())) {
            userIsChanged = true;
            updatedProfile.setAvatarUrl(updateUserDto.avatarUrl());
        }

        if(!updatedUser.getEmail().equals(updateUserDto.email())) {
            userIsChanged = true;
            updatedUser.setEmail(updateUserDto.email());
        }

        if (!updatedUser.getRole().getId().equals(updateUserDto.roleId())){
            userIsChanged = true;
            Role newRole = roleRepository.findById(updateUserDto.roleId())
                    .orElseThrow(() -> {
                        log.error("Role did not found in database! RoleId: {}", updateUserDto.roleId());
                        return new RoleNotExistException();
                    });
            updatedUser.setRole(newRole);
        }

        if (updatedUser.isActive() !=  updateUserDto.isActive()) {
            userIsChanged = true;
            updatedUser.setActive(updateUserDto.isActive());
        }

        if (updatedUser.isBlocked() != updateUserDto.isBlocked()) {
            userIsChanged = true;
            updatedUser.setBlocked(updateUserDto.isBlocked());
        }

        if (userIsChanged) {
            userRepository.save(updatedUser);
        }
        return userMapper.toDto(updatedUser);
    }

    /**
     * Changes the password for the specified user.
     *
     * <p>Verifies that the provided current password matches the stored password
     * before applying the change. The new password is encoded before persisting.</p>
     *
     * @param userId  the UUID of the user whose password is to be changed
     * @param request the request containing the current and new passwords
     * @throws UserNotFoundException          if no user with the given ID exists
     * @throws InvalidPasswordException       if the provided current password is incorrect
     */
    @Override
    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            log.error("Old passwords don't match!");
            throw new InvalidPasswordException();
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    /**
     * Resets the password for the specified user.
     *
     * <p>Administrative operation that sets a new password without requiring
     * the current password. Intended for use by administrators when a user
     * has lost access to their account.</p>
     *
     * <p>The new password is encoded before persisting.</p>
     *
     * @param userId  the UUID of the user whose password is to be reset
     * @param request the request containing the new password
     * @throws UserNotFoundException if no user with the given ID exists
     */
    @Override
    @Transactional
    public void resetPassword(UUID userId, ResetPasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }


}
