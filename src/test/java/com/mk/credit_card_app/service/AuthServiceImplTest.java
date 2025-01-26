package com.mk.credit_card_app.service;

import com.mk.credit_card_app.entity.Role;
import com.mk.credit_card_app.entity.User;
import com.mk.credit_card_app.exception.UserNotFoundException;
import com.mk.credit_card_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthServiceImpl authServiceImpl;

    @Test
    void testFindByUserName() {
        User user = User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        User userRes = authServiceImpl.findByUserName("test@gmail.com");
        assertNotNull(userRes);
        assertEquals("FCHPM0380R", userRes.getPan());
    }

    @Test
    void testFindByUserNameThrowsExcep() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> authServiceImpl.findByUserName("test@gmail.com"));
    }

    @Test
    void testGetAuthority() {
        User user = User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
        List<SimpleGrantedAuthority> grantedAuthorities = authServiceImpl.getAuthority(user);
        assertNotNull(grantedAuthorities);
    }

    @Test
    void testLoadUserByUsername(){
        User user = User.builder().userId("adhesksds").firstName("Test").lastName("est").pan("FCHPM0380R")
                .email("test@gmail.com").password("Password@1").role(Role.USER).build();
       when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetails userDetails = authServiceImpl.loadUserByUsername("test@gmail.com");
        assertNotNull(userDetails);
        assertEquals("Password@1",userDetails.getPassword());
    }

}
