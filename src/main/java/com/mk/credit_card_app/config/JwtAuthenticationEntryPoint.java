package com.mk.credit_card_app.config;

import ch.qos.logback.core.net.ObjectWriter;
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
import java.io.PrintWriter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException
            authException) throws IOException {
        ErrorResponse errorResponse=ErrorResponse.builder()
                .code(ErrorConstant.UNAUTHORIZED_CODE)
                .message(ErrorConstant.UNAUTHORIZED_MESSAGE).build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        log.error("Unauthorized exception");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}