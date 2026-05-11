package com.sitool.servicedesk.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * Utility class for writing JSON error responses to HttpServletResponse.
 */
public final class ErrorResponseWriter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ErrorResponseWriter() {
    }

    /**
     * Writes error response as JSON into HTTP response body.
     *
     * @param response      http response
     * @param errorResponse error payload
     * @throws IOException if writing response fails
     */
    public static void write(
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) throws IOException {

        response.setStatus(errorResponse.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        MAPPER.writeValue(response.getWriter(), errorResponse);
    }
}