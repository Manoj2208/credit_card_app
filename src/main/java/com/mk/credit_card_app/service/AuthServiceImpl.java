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

/**
 * Implementation of the {@link AuthService} and {@link UserDetailsService} interfaces
 * responsible for user authentication and loading user-specific data for Spring Security.
 * <p>
 * This class is annotated with {@code @Service} and is registered with the name "userService"
 * to match usage in Spring Security configuration.
 * </p>
 */
@Service(value = "userService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Finds a user by their email (used as username in this application).
     *
     * @param userName the email/username of the user
     * @return the matching {@link User} entity
     * @throws UserNotFoundException if no user with the given email is found
     */
    @Override
    public User findByUserName(String userName) {
        return userRepository.findByEmail(userName)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Retrieves the Spring Security authorities (roles) for the given user.
     *
     * @param user the user entity
     * @return a list containing a single {@link SimpleGrantedAuthority} based on the user's role
     */
    @Override
    public List<SimpleGrantedAuthority> getAuthority(User user) {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Loads the user-specific data required by Spring Security during authentication.
     *
     * @param username the username (email) used to authenticate the user
     * @return a Spring Security {@link UserDetails} instance with username, password, and authorities
     * @throws UsernameNotFoundException if no matching user is found
     */
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
