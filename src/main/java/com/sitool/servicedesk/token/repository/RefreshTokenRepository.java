package com.sitool.servicedesk.token.repository;

import com.sitool.servicedesk.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing RefreshToken entities.
 *
 * Provides access to refresh tokens for:
 * - authentication validation
 * - token rotation
 * - revocation handling
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    /**
     * Finds refresh token entity by its hashed value.
     *
     * @param tokenHash hashed refresh token
     * @return optional RefreshToken entity
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
