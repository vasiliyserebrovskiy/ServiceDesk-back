package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.exceptions.DefaultRoleNotExistException;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import com.sitool.servicedesk.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterUserResponse createNewUser(RegisterUserRequest registerUserRequest) {

        final String normalizedEmail = registerUserRequest.email().toLowerCase().trim();
        final String encodedPassword = passwordEncoder.encode(registerUserRequest.password());

        boolean userExist = userRepository.existsByEmail(normalizedEmail);

        if (userExist) throw new UserAlreadyExistException();

        User newUser = new User(registerUserRequest.firstname(), registerUserRequest.lastname(), normalizedEmail);
        Role role = roleRepository.findByDefaultRoleTrue()
                .orElseThrow(DefaultRoleNotExistException::new);

        newUser.setRole(role);
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

        return new RegisterUserResponse(
                newUser.getId().toString(),
                newUser.getFirstname(),
                newUser.getLastname(),
                newUser.getEmail(),
                newUser.getRole().getName());
    }
}
