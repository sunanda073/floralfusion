package com.floralfusion.floralfusion.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private String secretKey = "secret";  // Replace this with a more secure way to store the secret key
    private long expirationTime = 86400000;  // 24 hours in milliseconds

    // Generate JWT token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Validate the JWT token
    public boolean validateToken(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(getUsernameFromToken(token)) && !isTokenExpired(token));
    }

    // Extract username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Get expiration date from the token
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
