package com.mk.credit_card_app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for managing all application-wide exceptions in a centralized manner.
 * <p>
 * Extends {@link ResponseEntityExceptionHandler} to handle standard Spring exceptions like
 * validation failures, and uses {@link ExceptionHandler} for custom exceptions.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles validation errors for method arguments annotated with {@code @Valid}.
     *
     * @param ex      the {@link MethodArgumentNotValidException} thrown
     * @param headers HTTP headers
     * @param status  HTTP status code
     * @param request current web request
     * @return a response entity containing field-specific validation error messages
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles exceptions when a user already exists (conflict scenario).
     *
     * @param userConflictsException the thrown {@link UserConflictsException}
     * @return response with HTTP 409 Conflict and error message
     */
    @ExceptionHandler(UserConflictsException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotException(UserConflictsException userConflictsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .code(userConflictsException.getCode())
                        .message(userConflictsException.getMessage())
                        .build());
    }

    /**
     * Handles exceptions when a user is not found.
     *
     * @param userNotFound the thrown {@link UserNotFoundException}
     * @return response with HTTP 404 Not Found and error message
     */
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotException(UserNotFoundException userNotFound) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(userNotFound.getCode())
                        .message(userNotFound.getMessage())
                        .build());
    }

    /**
     * Handles exceptions when login credentials are invalid.
     *
     * @param invalidCredentialsException the thrown {@link InvalidCredentialsException}
     * @return response with HTTP 400 Bad Request and error message
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotException(InvalidCredentialsException invalidCredentialsException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(invalidCredentialsException.getCode())
                        .message(invalidCredentialsException.getMessage())
                        .build());
    }
}
