package com.sitool.servicedesk.security.service;

import com.sitool.servicedesk.token.dto.RefreshTokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtTokenService {

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    @Value("${jwt.at.live-in-min}")
    private int accessTokenLiveInMinutes;
    @Value("${jwt.rt.live-in-min}")
    private int refreshTokenLiveInMinutes;

    private final SecretKey accessTokenKey;
    private final SecretKey refreshTokenKey;

    public JwtTokenService(
            @Value("${jwt.at.secret}") String accessPhrase,
            @Value("${jwt.rt.secret}") String refreshPhrase
    ) {

        Objects.requireNonNull(accessPhrase, "Access token secret is null");
        Objects.requireNonNull(refreshPhrase, "Refresh token secret is null");
        if (accessPhrase.length() < 32 || refreshPhrase.length() < 32) {
            throw new IllegalArgumentException("Secrets must be at least 32 characters long");
        }
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
    }

    /**
     * Extracts subject (username) from given JWT token by type.
     */
    public String getUsernameFromToken(String token, TokenType tokenType) {
        SecretKey key = selectKey(tokenType);
        return extractClaim(token, Claims::getSubject, key);
    }

    /**
     * Generic extractor for any claim from token.
     */
    public <T> T extractClaim(String token,
                              Function<Claims, T> claimsResolver,
                              SecretKey key) {
        Claims claims = parseClaims(token, key);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate access token with username and roles.
     */
    public String generateAccessToken(String userEmail) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenLiveInMinutes * 60L);
        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(Date.from(expiry))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generate refresh token with username only.
     */
    public RefreshTokenDTO generateRefreshToken(String userEmail) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(refreshTokenLiveInMinutes * 60L);
        String refreshToken = Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(Date.from(expiry))
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();

        return new RefreshTokenDTO(refreshToken, now, expiry);
    }

    /**
     * Validate given token type.
     */
    public boolean validateToken(String token, TokenType tokenType) {
        try {
            parseClaims(token, selectKey(tokenType));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Internal: parse and verify token signature and expiration using old API.
     */
    private Claims parseClaims(String token, SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey selectKey(TokenType tokenType) {
        return tokenType == TokenType.ACCESS ? accessTokenKey : refreshTokenKey;
    }
}
