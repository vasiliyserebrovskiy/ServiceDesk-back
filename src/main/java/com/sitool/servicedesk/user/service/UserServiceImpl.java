package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.exceptions.RoleNotExistException;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import com.sitool.servicedesk.user.exceptions.UserNotFoundException;
import com.sitool.servicedesk.user.repository.UserRepository;
import com.sitool.servicedesk.userprofile.entity.UserProfile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.sitool.servicedesk.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        profile.setFirstname("admin");
        profile.setLastname("Administrator");
        profile.setDescription("Default administrator account. " +
                "Deactivate this account after creating a personal admin user.");

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
    }

    /**
     * Retrieves all users from the system.
     */
    @Override
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
    public UserDto getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(user);
    }


}
