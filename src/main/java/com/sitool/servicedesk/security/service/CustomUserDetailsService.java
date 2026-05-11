package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security {@link UserDetailsService}.
 *
 * <p>Loads application users from the database by email address
 * and converts them into {@link AuthUserDetails} objects
 * used by Spring Security authentication mechanism.</p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details by username (email).
     *
     * @param username user email used for authentication
     * @return authenticated user details
     * @throws UsernameNotFoundException if user with provided email does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return new AuthUserDetails(user);
    }
}
