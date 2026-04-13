package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapter for wrapping User entity into Spring Security UserDetails.
 */
public record AuthUserDetails(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
        return true; // account status not implemented yet
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // account status not implemented yet
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // account status not implemented yet
    }

    @Override
    public boolean isEnabled() {
        return true; // account status not implemented yet
    }
}
