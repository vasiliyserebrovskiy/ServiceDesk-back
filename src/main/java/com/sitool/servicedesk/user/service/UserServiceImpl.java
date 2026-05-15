package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.exceptions.DefaultRoleNotExistException;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
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
    public RegisterUserResponse createNewUser(RegisterUserRequest registerUserRequest) {

        final String normalizedEmail = registerUserRequest.email().toLowerCase().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistException();
        }

        Role role = roleRepository.findByDefaultRoleTrue()
                .orElseThrow(DefaultRoleNotExistException::new);

        String encodedPassword = passwordEncoder.encode(registerUserRequest.password());

        // 1. create user
        User newUser = new User(normalizedEmail, encodedPassword);
        newUser.setRole(role);
        newUser.setActive(true);
        newUser.setBlocked(false);

        // 2. create profile
        UserProfile profile = new UserProfile(registerUserRequest.firstname(), registerUserRequest.lastname());
        profile.setUser(newUser);

        // 3. set user profile
        newUser.setProfile(profile);

        userRepository.save(newUser);

        log.info("New User {} has been created.", newUser.getId());

        return userMapper.toRegisterResponse(newUser);
    }

    /**
     * Method for getting user information from database
     */
    public UserDto getUser(String email) {

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(currentUser);

    }
}
