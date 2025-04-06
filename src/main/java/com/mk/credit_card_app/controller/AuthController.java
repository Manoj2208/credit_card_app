package com.mk.credit_card_app.controller;

import com.mk.credit_card_app.config.JwtTokenUtil;
import com.mk.credit_card_app.dto.ApiResponse;
import com.mk.credit_card_app.dto.AuthRequest;
import com.mk.credit_card_app.dto.AuthResponse;
import com.mk.credit_card_app.entity.User;
import com.mk.credit_card_app.exception.InvalidCredentialsException;
import com.mk.credit_card_app.service.AuthService;
import com.mk.credit_card_app.util.SuccessConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication-related operations.
 * <p>
 * This controller provides an endpoint for user login and JWT token generation.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user and returns a JWT token upon successful login.
     *
     * @param authRequest the user credentials (username and password)
     * @return {@link ResponseEntity} containing the {@link AuthResponse} with the JWT token
     * @throws InvalidCredentialsException if authentication fails
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.userName(), authRequest.password())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        final User user = authService.findByUserName(authRequest.userName());
        final String jwtToken = jwtTokenUtil.generateToken(user, authService.getAuthority(user));

        AuthResponse response = AuthResponse.builder()
                .jwtToken(jwtToken)
                .response(ApiResponse.builder()
                        .httpStatus(SuccessConstant.LOGIN_SUCCESS_CODE)
                        .message(SuccessConstant.LOGIN_SUCCESS_MSG)
                        .build())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
