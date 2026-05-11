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
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

/**
 * Service responsible for creating, parsing and validating JWT tokens.
 *
 * <p>Supports two types of tokens:</p>
 * <ul>
 *     <li>ACCESS token - short-lived, used for authentication</li>
 *     <li>REFRESH token - long-lived, used to obtain new access tokens</li>
 * </ul>
 *
 * <p>Each token type is signed with its own secret key.</p>
 */
@Service
public class JwtTokenService {

    private final Clock clock;
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
            @Value("${jwt.rt.secret}") String refreshPhrase,
            Clock clock
    ) {

        Objects.requireNonNull(accessPhrase, "Access token secret is null");
        Objects.requireNonNull(refreshPhrase, "Refresh token secret is null");
        if (accessPhrase.length() < 32 || refreshPhrase.length() < 32) {
            throw new IllegalArgumentException("Secrets must be at least 32 characters long");
        }
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
        this.clock = clock;
    }

    /**
     * Extracts username (subject) from JWT token.
     *
     * @param token JWT token string
     * @param tokenType type of token (ACCESS or REFRESH)
     * @return username stored in token subject
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
     * Generates access token containing user email as subject.
     *
     * @param userEmail user identifier
     * @return signed JWT access token
     */
    public String generateAccessToken(String userEmail) {
        Instant now = Instant.now(clock);
        Instant expiry = now.plusSeconds(accessTokenLiveInMinutes * 60L);
        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(Date.from(expiry))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates refresh token with longer expiration time.
     *
     * @param userEmail user identifier
     * @return DTO containing refresh token and timestamps
     */
    public RefreshTokenDTO generateRefreshToken(String userEmail) {
        Instant now = Instant.now(clock);
        Instant expiry = now.plusSeconds(refreshTokenLiveInMinutes * 60L);
        String refreshToken = Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(Date.from(expiry))
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();

        return new RefreshTokenDTO(refreshToken, now, expiry);
    }

    /**
     * Validates token signature and expiration.
     *
     * @param token JWT token
     * @param tokenType token type (ACCESS or REFRESH)
     * @return true if token is valid, otherwise false
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
     * Parses JWT claims and validates token signature and expiration time.
     * Uses configured Clock instance for consistent time validation.
     */
    private Claims parseClaims(String token, SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setClock(() -> Date.from(clock.instant()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey selectKey(TokenType tokenType) {
        return tokenType == TokenType.ACCESS ? accessTokenKey : refreshTokenKey;
    }
}
