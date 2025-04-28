package com.mk.credit_card_app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.credit_card_app.exception.ErrorResponse;
import com.mk.credit_card_app.util.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom entry point for handling unauthorized access attempts in Spring Security.
 * <p>
 * This class is triggered when an unauthenticated user attempts to access a secured REST endpoint.
 * It constructs a custom {@link ErrorResponse} and writes it back in JSON format with HTTP 401 status.
 * </p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Handles the unauthorized error response when authentication fails.
     *
     * @param request       the {@link HttpServletRequest} that resulted in the authentication exception.
     * @param response      the {@link HttpServletResponse} to which the error response will be written.
     * @param authException the exception that caused the invocation.
     * @throws IOException if an input or output error occurs while writing the response.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorConstant.UNAUTHORIZED_CODE)
                .message(ErrorConstant.UNAUTHORIZED_MESSAGE)
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        log.error("Unauthorized exception");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
