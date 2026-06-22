package com.sitool.servicedesk.security.controller;

import com.sitool.servicedesk.security.dto.request.LoginUserRequest;
import com.sitool.servicedesk.security.dto.response.TokenResponseDto;
import com.sitool.servicedesk.security.service.AuthService;
import com.sitool.servicedesk.security.service.CookieService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import static com.sitool.servicedesk.security.constants.Constants.ACCESS_TOKEN_COOKIE;
import static com.sitool.servicedesk.security.constants.Constants.REFRESH_TOKEN_COOKIE;

/**
 * Implementation of authentication API.
 *
 * <p>This controller handles authentication flow using JWT stored in HttpOnly cookies.</p>
 *
 * <p>Responsibilities:
 * <ul>
 *     <li>Delegates authentication logic to AuthService</li>
 *     <li>Manages access and refresh tokens via cookies</li>
 *     <li>Handles logout by invalidating refresh token and clearing SecurityContext</li>
 * </ul>
 *
 * <p>All endpoints are stateless and rely on cookies instead of request bodies for token transport.</p>
 */
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final CookieService cookieService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Authenticates user and sets JWT tokens as HttpOnly cookies.
     */
    @Override
    public ResponseEntity<Void> login(LoginUserRequest loginUserRequest, HttpServletResponse response) {
        final TokenResponseDto tokens = authService.login(loginUserRequest);

        final ResponseCookie accessCookie = cookieService.generateAccessTokenCookie(tokens.accessToken());
        final ResponseCookie refreshCookie = cookieService.generateRefreshTokenCookie(tokens.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }

    /**
     * Issues new access and refresh tokens based on refresh token from cookie.
     */
    @Override
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.extractRefreshToken(request);

        final TokenResponseDto newAccessTokens = authService.refreshAccessToken(refreshToken);

        final ResponseCookie accessCookie = cookieService.generateAccessTokenCookie(newAccessTokens.accessToken());
        final ResponseCookie refreshCookie = cookieService.generateRefreshTokenCookie(newAccessTokens.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }

    /**
     * Invalidates refresh token, clears authentication cookies and SecurityContext.
     */
    @Override
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.extractRefreshToken(request);

        if (refreshToken != null) {
            refreshTokenService.logout(refreshToken);
        }

        final ResponseCookie accessCookie = cookieService.generateLogoutCookie(ACCESS_TOKEN_COOKIE);
        final ResponseCookie refreshCookie = cookieService.generateLogoutCookie(REFRESH_TOKEN_COOKIE);
        SecurityContextHolder.clearContext();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }
}