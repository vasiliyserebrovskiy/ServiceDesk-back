package com.sitool.servicedesk.security.filter;

import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;

import static com.sitool.servicedesk.security.constants.Constants.ACCESS_TOKEN_COOKIE;

/**
 * JWT authentication filter responsible for:
 * <ul>
 *     <li>extracting access token from cookies or Authorization header</li>
 *     <li>validating JWT token</li>
 *     <li>loading authenticated user details</li>
 *     <li>storing authentication in SecurityContext</li>
 * </ul>
 *
 * <p>The filter processes only access tokens.</p>
 */
@Log4j2 //for debugging
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    // Clear security context and stop filter chain
    // if access token is expired.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //DEBUG logs
//        log.info("====START DEBUGGING REQUEST =====");
//        log.info("Incoming {} {} — Cookie: {}", request.getMethod(), request.getRequestURI(), request.getHeader("Cookie"));
//        log.info("====STOP DEBUGGING REQUEST =====");

        String token = resolveToken(request);
        final JwtTokenService.TokenType tokenType = JwtTokenService.TokenType.ACCESS;

        if (StringUtils.isNoneBlank(token)) {
            try {
                if (jwtTokenService.validateToken(token, tokenType)) {
                    final String username = jwtTokenService.getUsernameFromToken(token, tokenType);
                    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                }
            } catch (ExpiredJwtException ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Resolves access token from:
     * <ul>
     *     <li>HTTP cookie</li>
     *     <li>Authorization Bearer header</li>
     * </ul>
     *
     * @return JWT access token or {@code null} if token is not present
     */
    private String resolveToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, ACCESS_TOKEN_COOKIE);
        if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
            return cookie.getValue();
        }
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            return StringUtils.substringAfter(authHeader, "Bearer ").trim();
        }

        return null;
    }
}
