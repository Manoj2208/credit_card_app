package com.mk.credit_card_app.service;

import com.mk.credit_card_app.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface AuthService {
    User findByUserName(String userName);

    List<SimpleGrantedAuthority> getAuthority(User user);
}
