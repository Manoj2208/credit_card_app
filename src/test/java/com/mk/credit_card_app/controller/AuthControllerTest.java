package com.mk.credit_card_app.controller;


import com.mk.credit_card_app.config.JwtTokenUtil;
import com.mk.credit_card_app.dto.AuthRequest;
import com.mk.credit_card_app.dto.AuthResponse;
import com.mk.credit_card_app.entity.Role;
import com.mk.credit_card_app.entity.User;
import com.mk.credit_card_app.exception.InvalidCredentialsException;
import com.mk.credit_card_app.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthControllerTest {
    @Mock
    AuthService authService;
    @Mock
    JwtTokenUtil jwtTokenUtil;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    AuthController authController;

    @Test
    void testGetToken(){
        String token="Token-wedwdu-s2";
        AuthRequest authRequest=AuthRequest.builder().userName("test@gmail.com")
                .password("Password@1").build();
        User user=User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
        List<SimpleGrantedAuthority> authorityList=List.of(new SimpleGrantedAuthority(user.getRole().name()));
        Authentication mockAuthentication = mock(Authentication.class);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authRequest.userName(),authRequest.password()))).thenReturn(mockAuthentication);
        when(authService.findByUserName(anyString())).thenReturn(user);
        when(authService.getAuthority(user)).thenReturn(authorityList);
        when(jwtTokenUtil.generateToken(user,authorityList)).thenReturn(token);
        ResponseEntity<AuthResponse> response=authController.getToken(authRequest);
        assertNotNull(response);
        assertEquals(token,response.getBody().jwtToken());
    }

    @Test
    void testGetTokenInvalidCredentials(){
        AuthRequest authRequest=AuthRequest.builder().userName("test@gmail.com")
                .password("Password@1").build();

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authRequest.userName(),authRequest.password()))).thenThrow(new InvalidCredentialsException());
        assertThrows(InvalidCredentialsException.class,()->authController.getToken(authRequest));
    }

}
