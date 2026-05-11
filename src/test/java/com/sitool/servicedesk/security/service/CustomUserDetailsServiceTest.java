package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {

        when(userRepository.findByEmailIgnoreCase("test@test.com"))
                .thenReturn(Optional.of(user));

        AuthUserDetails result =
                (AuthUserDetails) customUserDetailsService
                        .loadUserByUsername("test@test.com");

        assertNotNull(result);
        assertEquals("test@test.com", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {

        when(userRepository.findByEmailIgnoreCase("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService
                        .loadUserByUsername("missing@test.com")
        );
    }
}