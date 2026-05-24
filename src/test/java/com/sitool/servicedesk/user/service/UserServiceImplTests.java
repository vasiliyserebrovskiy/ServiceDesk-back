package com.sitool.servicedesk.user.service;

import com.sitool.servicedesk.role.entity.Role;

import com.sitool.servicedesk.role.exceptions.RoleNotExistException;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.user.dto.request.RegisterUserRequest;
import com.sitool.servicedesk.user.dto.request.UpdateUserDto;
import com.sitool.servicedesk.user.dto.response.UserDto;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.exceptions.UserAlreadyExistException;
import com.sitool.servicedesk.user.mapper.UserMapper;
import com.sitool.servicedesk.user.repository.UserRepository;
import com.sitool.servicedesk.userprofile.entity.UserProfile;
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

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * First successful test: New user created
     */
    @Test
    @DisplayName("Creating new user → user created successfully")
    void shouldCreateUserSuccessfully() {

        UUID id = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        RegisterUserRequest request =
                new RegisterUserRequest("John", "Doe", "test@mail.com", "1234", "USER", "","");

        Role role = new Role();
        role.setName("USER");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User user = i.getArgument(0);
            setId(user, id);
            return user;
        });
        when(userMapper.toDto(any(User.class)))
                .thenReturn(
                        new UserDto(
                                id,
                                "John",
                                "Doe",
                                "test@mail.com",
                                "some description",
                                "",
                                roleId,
                                true,
                                false
                        )
                );

        UserDto response = userService.createNewUser(request);

        assertEquals(id, response.id());
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("test@mail.com", response.email());
        assertEquals(roleId, response.roleId());
    }

    @Test
    @DisplayName("Creating user → email already exists → throws exception")
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterUserRequest request =
                new RegisterUserRequest("John", "Doe", "test@mail.com", "1234", "USER", "","");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class,
                () -> userService.createNewUser(request));

        verify(userRepository, never()).save(any());
        verify(roleRepository, never()).findByDefaultRoleTrue();
    }

    @Test
    @DisplayName("Creating user → role not found → throws exception")
    void shouldThrowExceptionWhenRoleNotFound() {

        RegisterUserRequest request =
                new RegisterUserRequest("John", "Doe", "test@mail.com", "1234", "TEST", "","");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(roleRepository.findByName("TEST")).thenReturn(Optional.empty());

        assertThrows(RoleNotExistException.class,
                () -> userService.createNewUser(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get current user by email → returns user dto")
    void shouldReturnCurrentUser() {

        String email = "test@mail.com";

        User user = new User();
        user.setEmail(email);

        UserDto dto = new UserDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                email,
                "",
                "",
                UUID.randomUUID(),
                true,
                false
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = userService.getMe(email);

        assertEquals(email, result.email());

        verify(userRepository).findByEmail(email);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Get user by id → returns user dto")
    void shouldReturnUserById() {

        UUID userId = UUID.randomUUID();

        User user = new User();

        UserDto dto = new UserDto(
                userId,
                "John",
                "Doe",
                "test@mail.com",
                "",
                "",
                UUID.randomUUID(),
                true,
                false
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = userService.getUser(userId);

        assertEquals(userId, result.id());
    }

    @Test
    @DisplayName("Get user by id → user not found")
    void shouldThrowExceptionWhenUserNotFound() {

        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                com.sitool.servicedesk.user.exceptions.UserNotFoundException.class,
                () -> userService.getUser(userId)
        );
    }

    @Test
    @DisplayName("Get all users → returns users list")
    void shouldReturnAllUsers() {

        User user1 = new User();
        User user2 = new User();

        UserDto dto1 = new UserDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john@mail.com",
                "",
                "",
                UUID.randomUUID(),
                true,
                false
        );

        UserDto dto2 = new UserDto(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "jane@mail.com",
                "",
                "",
                UUID.randomUUID(),
                true,
                false
        );

        when(userRepository.findAll())
                .thenReturn(java.util.List.of(user1, user2));

        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        var result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Update user → updates fields successfully")
    void shouldUpdateUserSuccessfully() {

        UUID userId = UUID.randomUUID();
        UUID oldRoleId = UUID.randomUUID();
        UUID newRoleId = UUID.randomUUID();

        Role oldRole = new Role();
        setId(oldRole, oldRoleId);

        Role newRole = new Role();
        setId(newRole, newRoleId);

        User user = new User();
        user.setEmail("old@mail.com");
        user.setActive(true);
        user.setBlocked(false);
        user.setRole(oldRole);

        com.sitool.servicedesk.userprofile.entity.UserProfile profile =
                new com.sitool.servicedesk.userprofile.entity.UserProfile();

        profile.setFirstname("Old");
        profile.setLastname("Name");
        profile.setDescription("Old description");
        profile.setAvatarUrl("old-avatar");

        user.setProfile(profile);

        UpdateUserDto updateDto = new UpdateUserDto(
                "New",
                "User",
                "new@mail.com",
                newRoleId,
                false,
                true,
                "New description",
                "new-avatar"
        );

        UserDto responseDto = new UserDto(
                userId,
                "New",
                "User",
                "new@mail.com",
                "New description",
                "new-avatar",
                newRoleId,
                false,
                true
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(roleRepository.findById(newRoleId))
                .thenReturn(Optional.of(newRole));

        when(userMapper.toDto(user))
                .thenReturn(responseDto);

        UserDto result = userService.updateUser(userId, updateDto);

        assertEquals("new@mail.com", result.email());
        assertEquals("New", result.firstname());

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Update user → role not found")
    void shouldThrowExceptionWhenUpdatingWithUnknownRole() {

        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        Role oldRole = new Role();
        setId(oldRole, UUID.randomUUID());

        User user = new User();
        user.setRole(oldRole);
        user.setActive(true);
        user.setBlocked(false);
        user.setEmail("test@test.com");

        UserProfile profile =
                new UserProfile();

        profile.setFirstname("John");
        profile.setLastname("Doe");
        profile.setDescription("John description");
        profile.setAvatarUrl("http://example.com");

        user.setProfile(profile);

        UpdateUserDto dto = new UpdateUserDto(
                "John",
                "Doe",
                "mail@test.com",
                roleId,
                true,
                false,
                "",
                ""
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        assertThrows(
                RoleNotExistException.class,
                () -> userService.updateUser(userId, dto)
        );
    }

    @Test
    @DisplayName("Create admin if not exists → creates admin")
    void shouldCreateAdminIfNotExists() {

        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        when(userRepository.existsByEmail("admin@domain.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("P@ssw0rd"))
                .thenReturn("encoded");

        when(roleRepository.findByName("ADMIN"))
                .thenReturn(Optional.of(adminRole));

        userService.createAdminIfNotExists();

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Create admin if already exists → do nothing")
    void shouldNotCreateAdminIfAlreadyExists() {

        when(userRepository.existsByEmail("admin@domain.com"))
                .thenReturn(true);

        userService.createAdminIfNotExists();

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
