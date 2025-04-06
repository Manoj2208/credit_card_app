package com.mk.credit_card_app.config;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.mk.credit_card_app.entity.User;
import com.mk.credit_card_app.util.Constants;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Utility class for generating, parsing, and validating JWT tokens.
 * <p>
 * It provides methods to extract claims, validate token expiration, and build custom tokens
 * with scopes (authorities).
 * </p>
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username contained in the token
     */
    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim using a resolver function.
     *
     * @param token         the JWT token
     * @param claimResolver a function that extracts the desired claim from the token's claims
     * @param <T>           the type of the claim
     * @return the extracted claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Retrieves the expiration date of the token.
     *
     * @param token the JWT token
     * @return the expiration {@link Date}
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Generates a new JWT token for a given user and their authorities.
     *
     * @param user        the user entity
     * @param authorities the list of granted authorities (roles)
     * @return a signed JWT token string
     */
    public String generateToken(User user, List<SimpleGrantedAuthority> authorities) {
        return generateToken(user.getEmail(), authorities);
    }

    /**
     * Validates the token by comparing the username and checking for expiration.
     *
     * @param token   the JWT token
     * @param details the {@link UserDetails} for comparison
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails details) {
        final String username = getUserNameFromToken(token);
        return (username.equals(details.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Retrieves all claims from the token using the secret signing key.
     *
     * @param token the JWT token
     * @return the {@link Claims} object
     */
    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Constants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks whether the token has expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        final Date date = getExpirationDateFromToken(token);
        return date.before(new Date());
    }

    /**
     * Internal method to generate a token with subject and scopes.
     *
     * @param subject     the subject (typically email/username)
     * @param authorities the list of authorities to include in the token claims
     * @return the generated JWT token
     */
    private String generateToken(String subject, List<SimpleGrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .compact();
    }
}
