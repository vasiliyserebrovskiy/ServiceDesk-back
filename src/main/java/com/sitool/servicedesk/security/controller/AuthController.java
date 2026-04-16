package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.request.RefreshTokenRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.security.service.AuthService;
import com.sitool.servicedesk.security.service.CookieService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final AuthService authService;
    private final CookieService cookieService;

    @Override
    public TokenResponseDto login(@Valid @RequestBody LoginUserRequest loginUserRequest, HttpServletResponse response) {

        final TokenResponseDto tokens = authService.login(loginUserRequest);

        final Cookie accessCookie = cookieService.generateAccessTokenCookie(tokens.accessToken());
        final Cookie refreshCookie = cookieService.generateRefreshTokenCookie(tokens.refreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return tokens;
    }

    @Override
    public TokenResponseDto refresh(@RequestBody RefreshTokenRequest refreshToken, HttpServletResponse response) {

        final TokenResponseDto newAccessTokens = authService.refreshAccessToken(refreshToken.refreshToken());

        final Cookie accessCookie = cookieService.generateAccessTokenCookie(newAccessTokens.accessToken());
        final Cookie refreshCookie = cookieService.generateRefreshTokenCookie(newAccessTokens.refreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new TokenResponseDto(newAccessTokens.accessToken(), newAccessTokens.refreshToken());

    }


}
