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

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(User user, List<SimpleGrantedAuthority> authorities) {
        return generateToken(user.getEmail(), authorities);
    }

    public Boolean validateToken(String token, UserDetails details) {
        final String username = getUserNameFromToken(token);
        return (username.equals(details.getUsername()) && !isTokenExpired(token));
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(Constants.SIGNING_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date date = getExpirationDateFromToken(token);
        return date.before(new Date());
    }

    private String generateToken(String subject, List<SimpleGrantedAuthority> authorities) {

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", authorities);

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY).compact();
    }

}
