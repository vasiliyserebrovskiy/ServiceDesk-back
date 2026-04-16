package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import com.sitool.servicedesk.exceptions.common.RestApiException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public TokenResponseDto login(LoginUserRequest loginRequest) {
        String userEmail = loginRequest.email();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails, loginRequest.password())
            );
        } catch (DisabledException ex) {
           // log.warn("Login attempt for inactive account: {}", userEmail, ex);
            throw new RestApiException(
                    HttpStatus.FORBIDDEN,
                    "User account is not active. Please confirm your email."
            );
        } catch (LockedException ex) {
           // log.warn("Login attempt for locked account: {}", userEmail, ex);
            throw new RestApiException(
                    HttpStatus.FORBIDDEN,
                    "User account is locked."
            );
        } catch (BadCredentialsException ex) {
           // log.warn("Bad credentials for user: {}", userEmail, ex);
            throw new RestApiException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password."
            );
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenService.generateAccessToken(userEmail);
        String refreshToken = jwtTokenService.generateRefreshToken(userEmail);
        //TODO:Save refreshToken in DB
        return new TokenResponseDto(accessToken, refreshToken);
    }


    public TokenResponseDto refreshAccessToken(String refreshToken) {
        if (jwtTokenService.validateToken(refreshToken, JwtTokenService.TokenType.REFRESH)) {
            String username = jwtTokenService.getUsernameFromToken(refreshToken, JwtTokenService.TokenType.REFRESH);
            String newAccessToken = jwtTokenService.generateAccessToken(username);
            String newRefreshToken = jwtTokenService.generateRefreshToken(username);
            //TODO: update refreshToken in BD
            return new TokenResponseDto(newAccessToken, newRefreshToken);
        }
        throw new RestApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

}
