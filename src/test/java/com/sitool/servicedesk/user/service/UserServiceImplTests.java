package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.exceptions.DefaultRoleNotExistException;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.response.RegisterUserResponse;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import com.sitool.servicedesk.user.repository.UserRepository;
import com.sitool.servicedesk.utils.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * First successful test: New user created
     */
    @Test
    @DisplayName("Creating new user → user created successfully")
    void shouldCreateUserSuccessfully() {

        UUID id = UUID.randomUUID();

        RegisterUserRequest request =
                new RegisterUserRequest("John", "Doe", "test@mail.com", "1234");

        Role role = new Role();
        role.setName("USER");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(roleRepository.findByDefaultRoleTrue()).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User user = i.getArgument(0);
            setId(user, id);
            return user;
        });

        RegisterUserResponse response = userService.createNewUser(request);

        assertEquals(id.toString(), response.id());
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("test@mail.com", response.email());
        assertEquals("USER", response.role());
    }

    @Test
    @DisplayName("Creating user → email already exists → throws exception")
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterUserRequest request =
                new RegisterUserRequest("John", "Doe", "test@mail.com", "1234");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class,
                () -> userService.createNewUser(request));

        verify(userRepository, never()).save(any());
        verify(roleRepository, never()).findByDefaultRoleTrue();
    }

    @Test
    @DisplayName("Creating user → default role not found → throws exception")
    void shouldThrowExceptionWhenDefaultRoleNotFound() {

        RegisterUserRequest request =
                new RegisterUserRequest("John", "Doe", "test@mail.com", "1234");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(roleRepository.findByDefaultRoleTrue()).thenReturn(Optional.empty());

        assertThrows(DefaultRoleNotExistException.class,
                () -> userService.createNewUser(request));

        verify(userRepository, never()).save(any());
    }

    private void setId(Object entity, UUID id) {
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
