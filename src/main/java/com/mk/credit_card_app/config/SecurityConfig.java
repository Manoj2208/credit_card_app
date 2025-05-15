package com.mk.credit_card_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security settings.
 * <p>
 * This config sets up authentication and authorization filters,
 * disables CSRF and CORS, and registers JWT-based stateless security.
 * </p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint point;

    @Autowired
    private KeycloakRoleConverter keycloakRoleConverter;

    /**
     * Defines the password encoder bean using BCrypt hashing algorithm.
     *
     * @return {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Configures the security filter chain for HTTP requests.
     * <ul>
     *     <li>Disables CSRF and CORS</li>
     *     <li>Secures endpoints under <code>/api/v1/credit-cards/**</code></li>
     *     <li>Allows public access to Swagger UI and login endpoints</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the built {@link SecurityFilterChain}
     * @throws Exception if an error occurs during setup
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakRoleConverter);
        
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/credit-cards/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(authServer->authServer
                        .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                        .authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
