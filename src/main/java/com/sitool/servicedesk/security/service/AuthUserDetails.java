package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security adapter for exposing application User entity
 * as UserDetails implementation.
 *
 * <p>Provides authentication and authorization data required by
 * Spring Security during login and request processing.</p>
 */
public record AuthUserDetails(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security expects roles to be prefixed with ROLE_
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // account expiration policy is not used
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // account locking is not implemented yet
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credential expiration policy is not used
    }

    @Override
    public boolean isEnabled() {
        return true; // user activation status is not implemented yet
    }
}
