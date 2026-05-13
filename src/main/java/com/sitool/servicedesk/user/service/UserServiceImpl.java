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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.sitool.servicedesk.user.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
    Method for creating new user
     */
    @Override
    @Transactional
    public RegisterUserResponse createNewUser(RegisterUserRequest registerUserRequest) {

        final String normalizedEmail = registerUserRequest.email().toLowerCase().trim();
        boolean userExist = userRepository.existsByEmail(normalizedEmail);
        if (userExist) throw new UserAlreadyExistException();

        final String encodedPassword = passwordEncoder.encode(registerUserRequest.password());


        User newUser = new User(registerUserRequest.firstname(), registerUserRequest.lastname(), normalizedEmail);
        Role role = roleRepository.findByDefaultRoleTrue()
                .orElseThrow(DefaultRoleNotExistException::new);

        newUser.setRole(role);
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

        return userMapper.toRegisterResponse(newUser);

    }

    /**
     * Method for getting user information from database
     */
    public UserDto getUser(String email) {

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        String temp = currentUser.getAvatarUrl();

        return userMapper.toDto(currentUser);

    }
}
