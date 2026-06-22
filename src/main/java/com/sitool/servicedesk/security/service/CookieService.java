package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.security.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Service responsible for creating and extracting
 * authentication cookies used in JWT-based authorization.
 *
 * <p>Handles:
 * <ul>
 *     <li>Access token cookies</li>
 *     <li>Refresh token cookies</li>
 *     <li>Logout cookie invalidation</li>
 *     <li>Refresh token extraction from requests</li>
 * </ul>
 * </p>
 */
@Service
public class CookieService {

    @Value("${jwt.at.live-in-min}")
    private int accessTokenLiveInMinutes;
    @Value("${jwt.rt.live-in-min}")
    private int refreshTokenLiveInMinutes;

    /**
     * Creates cookie that invalidates existing auth cookie on client side.
     *
     * @param cookieName cookie name to invalidate
     * @return expired cookie with maxAge = 0
     */
    public ResponseCookie generateLogoutCookie(final String cookieName) {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
    }

    /**
     * Creates HTTP-only cookie containing JWT access token.
     *
     * @param accessToken generated JWT access token
     * @return configured access token cookie
     */
    public ResponseCookie generateAccessTokenCookie(final String accessToken) {
        return ResponseCookie.from(Constants.ACCESS_TOKEN_COOKIE, accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(convertMinutesToSeconds(accessTokenLiveInMinutes))
                .sameSite("None")
                .build();
    }

    /**
     * Creates HTTP-only cookie containing refresh token.
     *
     * @param refreshToken generated refresh token
     * @return configured refresh token cookie
     */
    public ResponseCookie generateRefreshTokenCookie(final String refreshToken) {
        return ResponseCookie.from(Constants.REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(convertMinutesToSeconds(refreshTokenLiveInMinutes))
                .sameSite("None")
                .build();
    }

    /**
     * Extracts refresh token value from request cookies.
     *
     * @param request incoming HTTP request
     * @return refresh token value or null if cookie is absent
     */
    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> Constants.REFRESH_TOKEN_COOKIE.equals(cookie.getName()))
                .map(jakarta.servlet.http.Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private static int convertMinutesToSeconds(int minutes) {
        return minutes * 60;
    }
}