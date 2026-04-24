package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.security.constants.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieService {

    @Value("${jwt.at.live-in-min}")
    private int accessTokenLiveInMinutes;
    @Value("${jwt.rt.live-in-min}")
    private int refreshTokenLiveInMinutes;
    @Value("${security.cookies.secure:true}")
    private boolean cookieSecure;

    public Cookie generateLogoutCookie(final String cookieName) {
        final Cookie cookie = new Cookie(cookieName, null);
        configureCommonCookieSettings(cookie);
        cookie.setMaxAge(0);
        return cookie;
    }

    public Cookie generateAccessTokenCookie(final String accessToken) {
        final Cookie cookie = new Cookie(Constants.ACCESS_TOKEN_COOKIE, accessToken);
        configureCommonCookieSettings(cookie);
        cookie.setMaxAge(convertMinutesToSeconds(accessTokenLiveInMinutes));
        return cookie;
    }

    public Cookie generateRefreshTokenCookie(final String refreshToken) {
        final Cookie cookie = new Cookie(Constants.REFRESH_TOKEN_COOKIE, refreshToken);
        configureCommonCookieSettings(cookie);
        cookie.setMaxAge(convertMinutesToSeconds(refreshTokenLiveInMinutes));
        return cookie;
    }

    private void configureCommonCookieSettings(final Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
    }

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

    private int convertMinutesToSeconds(int minutes) {
        return minutes * 60;
    }
}
