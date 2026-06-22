package com.sitool.servicedesk.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * CORS configuration for the application.
 *
 * <p>Allows cross-origin requests from the frontend application
 * running on http://localhost:5173 (e.g. React/Vite dev server).</p>
 *
 * <p>Configures allowed HTTP methods, headers, and enables credentials
 * support (cookies / authorization headers).</p>
 */
@Configuration
public class CorsConfig {

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://service-desk-vasiliy-serebrovskiys-projects.vercel.app",
                "https://service-desk-git-main-vasiliy-serebrovskiys-projects.vercel.app", "https://service-desk-i3dlwfbjz-vasiliy-serebrovskiys-projects.vercel.app", "https://service-desk-beryl.vercel.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
