package com.jeffersonvilla.HabitsTracker.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.function.Function;
import java.util.HashMap;
import java.util.Map;

@Service 
public class JwtService {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY = "600e6e3c514e3f57e8c4860244e4505d9220f5eda8104b1c3446d3b41e3f19d8"; //Value assigned for testing purposes
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    private final String USER_ID_CLAIM = "userId";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    } 

    public String generateToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_CLAIM, userId);
        return createToken(claims, username);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSigningKey())
        .compact();
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
        .verifyWith((SecretKey) getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(USER_ID_CLAIM, Long.class);
    }
}
