package com.sitool.servicedesk.security.config;

import com.sitool.servicedesk.security.filter.JwtTokenFilter;
import com.sitool.servicedesk.security.handler.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central security configuration for the application.
 *
 * <p>Defines authentication and authorization rules, including:
 * <ul>
 *     <li>JWT-based authentication</li>
 *     <li>Public endpoints (Swagger, auth, registration)</li>
 *     <li>Protected API endpoints requiring authentication</li>
 * </ul>
 *
 * <p>Also configures password encoding and exception handling
 * for unauthorized requests.</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    /**
     * Password encoder bean used for hashing user passwords.
     *
     * <p>Uses BCrypt algorithm for secure password storage.</p>
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides AuthenticationManager from Spring Security configuration.
     *
     * <p>Used for processing authentication requests (e.g. login).</p>
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the Spring Security filter chain.
     *
     * <p>Security rules:
     * <ul>
     *     <li>CSRF disabled (stateless REST API)</li>
     *     <li>CORS enabled via external configuration</li>
     *     <li>Public access to Swagger endpoints</li>
     *     <li>Public access to authentication endpoints (login, register, refresh token)</li>
     *     <li>All other endpoints require authentication</li>
     * </ul>
     *
     * <p>JWT token filter is applied before UsernamePasswordAuthenticationFilter
     * to handle token-based authentication.</p>
     *
     * <p>Custom authentication entry point is used for returning REST-friendly
     * error responses instead of default HTML login page.</p>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        //Swagger API
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                        //Register user
                        //.requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                        //Login user
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh-token").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
