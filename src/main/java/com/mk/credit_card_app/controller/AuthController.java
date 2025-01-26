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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.userName(), authRequest.password()));
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
        final User user = authService.findByUserName(authRequest.userName());
        final String jwtToken = jwtTokenUtil.generateToken(user, authService.getAuthority(user));
        return ResponseEntity.status(HttpStatus.OK).body(AuthResponse.builder().jwtToken(jwtToken).response(ApiResponse
                .builder().httpStatus(SuccessConstant.LOGIN_SUCCESS_CODE).message(SuccessConstant.LOGIN_SUCCESS_MSG)
                .build()).build());
    }
}
