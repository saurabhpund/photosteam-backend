package com.example.ecommerce_webapp.utils;

import com.example.ecommerce_webapp.model.User;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my_super_secret_key_which_should_be_very_secure_and_long_256bit_key!!";
    private SecretKey key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = Map.of(
                "roles", User.getRoles().stream()
                        .map(role -> "ROLE_" + role.name())
                        .collect(Collectors.toList())
        );
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getParser().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getParser().parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

    private JwtParser getParser() {
        return Jwts.parser().verifyWith(key).build();
    }
}
