package com.mk.credit_card_app.config;

import com.mk.credit_card_app.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader(Constants.HEADER_STRING);

        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith(Constants.TOKEN_PREFIX)) {
            token = requestHeader.substring(7);

            try {
                // Extract username from token
                username = jwtTokenUtil.getUserNameFromToken(token);
            } catch (IllegalArgumentException e) {
                log.error("Illegal argument while fetching the username from token", e);
            } catch (ExpiredJwtException e) {
                log.warn("JWT token has expired", e);
            } catch (MalformedJwtException e) {
                log.error("Malformed token, invalid JWT", e);
            } catch (Exception e) {
                log.error("Unexpected error while processing JWT token", e);
            }
        } else {
            log.warn("No Bearer token found in the Authorization header");
        }

        // Process authentication if username and SecurityContext are valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(token, userDetails))) {
                // Extract roles dynamically from JWT claims
                Claims claims = jwtTokenUtil.getAllClaims(token);
                List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);

                // Set the authentication in SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("User '{}' authenticated successfully with roles: {}", username, authorities);
            } else {
                log.warn("Token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        Object scopes = claims.get("scopes");
        if (scopes instanceof List<?>) {
            return ((List<?>) scopes)
                    .stream()
                    .map(role -> {
                        // Check if role is a map and extract the "authority" key
                        if (role instanceof Map<?, ?> roleMap) {
                            Object authority = roleMap.get("authority");
                            if (authority != null) {
                                // Log the authority to debug
                                log.debug("Extracted authority from map: {}", authority.toString());
                                return new SimpleGrantedAuthority("ROLE_" + authority.toString());
                            }
                        } else if (role instanceof String) {
                            // Handle case where role is a plain string
                            log.debug("Extracted role: {}", role.toString());
                            return new SimpleGrantedAuthority("ROLE_" + role.toString());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)  // Filter out any null values
                    .toList();
        }
        return Collections.emptyList();
    }

}
