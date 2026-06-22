package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.security.constants.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class CookieServiceTest {

    private CookieService cookieService;

    @BeforeEach
    void setUp() {
        cookieService = new CookieService();
        ReflectionTestUtils.setField(cookieService, "accessTokenLiveInMinutes", 15);
        ReflectionTestUtils.setField(cookieService, "refreshTokenLiveInMinutes", 60);
    }

    @Test
    void generateAccessTokenCookie_shouldCreateValidCookie() {
        ResponseCookie cookie = cookieService.generateAccessTokenCookie("access-token");

        assertNotNull(cookie);
        assertEquals(Constants.ACCESS_TOKEN_COOKIE, cookie.getName());
        assertEquals("access-token", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(15 * 60, cookie.getMaxAge().getSeconds());
        assertEquals("None", cookie.getSameSite());
    }

    @Test
    void generateRefreshTokenCookie_shouldCreateValidCookie() {
        ResponseCookie cookie = cookieService.generateRefreshTokenCookie("refresh-token");

        assertNotNull(cookie);
        assertEquals(Constants.REFRESH_TOKEN_COOKIE, cookie.getName());
        assertEquals("refresh-token", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(60 * 60, cookie.getMaxAge().getSeconds());
        assertEquals("None", cookie.getSameSite());
    }

    @Test
    void generateLogoutCookie_shouldCreateExpiredCookie() {
        ResponseCookie cookie = cookieService.generateLogoutCookie("access");

        assertNotNull(cookie);
        assertEquals("access", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge().getSeconds());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
        assertEquals("None", cookie.getSameSite());
    }

    @Test
    void extractRefreshToken_shouldReturnToken_whenCookieExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(
                new Cookie("other", "123"),
                new Cookie(Constants.REFRESH_TOKEN_COOKIE, "refresh-token")
        );

        String result = cookieService.extractRefreshToken(request);
        assertEquals("refresh-token", result);
    }

    @Test
    void extractRefreshToken_shouldReturnNull_whenCookiesAbsent() {
        HttpServletRequest request = new MockHttpServletRequest();

        String result = cookieService.extractRefreshToken(request);
        assertNull(result);
    }

    @Test
    void extractRefreshToken_shouldReturnNull_whenRefreshCookieMissing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access", "access-token"));

        String result = cookieService.extractRefreshToken(request);
        assertNull(result);
    }
}