package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.request.RefreshTokenRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.security.service.AuthService;
import com.sitool.servicedesk.security.service.CookieService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static com.sitool.servicedesk.security.constants.Constants.ACCESS_TOKEN_COOKIE;
import static com.sitool.servicedesk.security.constants.Constants.REFRESH_TOKEN_COOKIE;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final AuthService authService;
    private final CookieService cookieService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenResponseDto login(LoginUserRequest loginUserRequest, HttpServletResponse response) {

        final TokenResponseDto tokens = authService.login(loginUserRequest);

        final Cookie accessCookie = cookieService.generateAccessTokenCookie(tokens.accessToken());
        final Cookie refreshCookie = cookieService.generateRefreshTokenCookie(tokens.refreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return tokens;
    }

    @Override
    public TokenResponseDto refresh(RefreshTokenRequest refreshToken, HttpServletResponse response) {

        final TokenResponseDto newAccessTokens = authService.refreshAccessToken(refreshToken.refreshToken());

        final Cookie accessCookie = cookieService.generateAccessTokenCookie(newAccessTokens.accessToken());
        final Cookie refreshCookie = cookieService.generateRefreshTokenCookie(newAccessTokens.refreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new TokenResponseDto(newAccessTokens.accessToken(), newAccessTokens.refreshToken());

    }

    @Override
    public TokenResponseDto logout(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieService.extractRefreshToken(request);

        if (refreshToken != null) {
            refreshTokenService.logout(refreshToken);
        }

        final Cookie accessCookie = cookieService.generateLogoutCookie(ACCESS_TOKEN_COOKIE);
        final Cookie refreshCookie = cookieService.generateLogoutCookie(REFRESH_TOKEN_COOKIE);
        SecurityContextHolder.clearContext();

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new TokenResponseDto(null, null);
    }


}
