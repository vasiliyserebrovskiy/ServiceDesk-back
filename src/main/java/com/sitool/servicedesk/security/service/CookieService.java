package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.security.constants.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${security.cookies.secure:true}")
    private boolean cookieSecure;

    /**
     * Creates cookie that invalidates existing auth cookie on client side.
     *
     * @param cookieName cookie name to invalidate
     * @return expired cookie with maxAge = 0
     */
    public Cookie generateLogoutCookie(final String cookieName) {
        final Cookie cookie = new Cookie(cookieName, null);
        configureCommonCookieSettings(cookie);
        cookie.setMaxAge(0);
        return cookie;
    }

    /**
     * Creates HTTP-only cookie containing JWT access token.
     *
     * @param accessToken generated JWT access token
     * @return configured access token cookie
     */
    public Cookie generateAccessTokenCookie(final String accessToken) {
        final Cookie cookie = new Cookie(Constants.ACCESS_TOKEN_COOKIE, accessToken);
        configureCommonCookieSettings(cookie);
        cookie.setMaxAge(convertMinutesToSeconds(accessTokenLiveInMinutes));
        return cookie;
    }

    /**
     * Creates HTTP-only cookie containing refresh token.
     *
     * @param refreshToken generated refresh token
     * @return configured refresh token cookie
     */
    public Cookie generateRefreshTokenCookie(final String refreshToken) {
        final Cookie cookie = new Cookie(Constants.REFRESH_TOKEN_COOKIE, refreshToken);
        configureCommonCookieSettings(cookie);
        cookie.setMaxAge(convertMinutesToSeconds(refreshTokenLiveInMinutes));
        return cookie;
    }

    /**
     * Applies common security settings for authentication cookies.
     */
    private void configureCommonCookieSettings(final Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
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
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private static int convertMinutesToSeconds(int minutes) {
        return minutes * 60;
    }
}
