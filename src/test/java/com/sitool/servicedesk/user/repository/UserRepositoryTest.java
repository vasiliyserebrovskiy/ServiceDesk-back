package com.sitool.servicedesk.user.repository;

import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.role.repository.RoleRepository;
import com.sitool.servicedesk.userprofile.entity.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role createRole() {
        Role role = new Role();
        role.setName("USER");
        role.setDisplayName("User");
        role.setDescription("Default role");
        role.setDefaultRole(true);
        return roleRepository.save(role);
    }

    private User createUser(String email) {
        Role role = createRole();

        User user = new User();
        UserProfile profile = new UserProfile();
        profile.setFirstname("John");
        profile.setLastname("Doe");
        profile.setUser(user);
        user.setEmail(email);
        user.setPassword("encoded-password");
        user.setRole(role);
        user.setProfile(profile);
        user.setActive(true);


        return userRepository.save(user);
    }

    @Test
    @DisplayName("Should return true when user exists by email")
    void shouldReturnTrueWhenUserExistsByEmail() {

        createUser("test@example.com");

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when user does not exist by email")
    void shouldReturnFalseWhenUserDoesNotExistByEmail() {

        boolean exists = userRepository.existsByEmail("missing@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find user by email ignoring case")
    void shouldFindUserByEmailIgnoreCase() {

        createUser("Test@Example.com");

        Optional<User> result =
                userRepository.findByEmailIgnoreCase("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("Test@Example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when email not found (ignore case)")
    void shouldReturnEmptyWhenEmailNotFoundIgnoreCase() {

        Optional<User> result =
                userRepository.findByEmailIgnoreCase("missing@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find user by exact email")
    void shouldFindUserByEmail() {

        createUser("exact@example.com");

        User result = userRepository.findByEmail("exact@example.com")
                .orElseThrow(() -> new AssertionError("User should exist"));

        assertEquals("exact@example.com", result.getEmail());
    }
}