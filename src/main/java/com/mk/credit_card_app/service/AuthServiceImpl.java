package com.mk.credit_card_app.service;

import com.mk.credit_card_app.entity.User;
import com.mk.credit_card_app.exception.UserNotFoundException;
import com.mk.credit_card_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByEmail(userName)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthority(User user) {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(getAuthority(user))
                .build();
    }
}
