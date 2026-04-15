package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.request.RefreshTokenRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public TokenResponseDto login(@Valid @RequestBody LoginUserRequest loginUserRequest) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.email(), loginUserRequest.password())
        );

        UserDetails user = (UserDetails) auth.getPrincipal(); // use Authentication

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponseDto(token, refreshToken);
    }

    @Override
    public TokenResponseDto refresh(@RequestBody RefreshTokenRequest refreshToken) {
        System.out.println("REQUEST: " + refreshToken.toString());
        String token = refreshToken.refreshToken();
        System.out.println("TOKEN: " + token);
        String userName = jwtService.extractUsername(token);
        System.out.println("UserName: " + userName);
        UserDetails user = userDetailsService.loadUserByUsername(userName);

        if (!jwtService.isTokenValid(token, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }


}
